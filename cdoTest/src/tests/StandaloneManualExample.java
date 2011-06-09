/**
 * http://www.eclipsezone.com/eclipse/forums/t111170.html
 * 
 * CVS: /org.eclipse.emf.cdo.examples/src/org/eclipse/emf/cdo/examples/StandaloneContainerExample.java
 */
package tests;

import static java.lang.System.out;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.common.protocol.CDOAuthenticator;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.net4j.CDOSessionConfiguration;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.net4j.FactoriesProtocolProvider;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.buffer.IBufferProvider;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;
import org.eclipse.net4j.util.security.IPasswordCredentials;
import org.eclipse.net4j.util.security.IPasswordCredentialsProvider;

import biz.tradescape.mmt.model.marketplace.EntityType;
import biz.tradescape.mmt.model.marketplace.MarketplacePackage;

/**
 * http://www.eclipsezone.com/eclipse/forums/t111170.html
 */
public class StandaloneManualExample
{
  final static String cdo_server = "tcp://localhost:2042/?repositoryName=marketplace1";

  final static String cdo_user = "anonymous";

  final static String cdo_pass = "passw";

  final static String cdo_repo = "marketplace1";
  
  final static String MARKETPLACE = "marketplace";
  final static String PARTICIPANTS="participants";

  public static void main(String[] args) throws Exception
  {
    // Enable logging and tracing
    OMPlatform.INSTANCE.setDebugging(true);
    OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
    OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);

    // Prepare receiveExecutor
    final ThreadGroup threadGroup = new ThreadGroup("net4j"); //$NON-NLS-1$
    ExecutorService receiveExecutor = Executors.newCachedThreadPool(new ThreadFactory()
    {
      public Thread newThread(Runnable r)
      {
        Thread thread = new Thread(threadGroup, r);
        thread.setDaemon(true);
        return thread;
      }
    });

    // Prepare bufferProvider
    IBufferProvider bufferProvider = Net4jUtil.createBufferPool();
    LifecycleUtil.activate(bufferProvider);

    FactoriesProtocolProvider protocolProvider = new FactoriesProtocolProvider(
        new org.eclipse.emf.cdo.internal.net4j.protocol.CDOClientProtocolFactory());
    
    // Prepare selector
    org.eclipse.net4j.internal.tcp.TCPSelector selector = new org.eclipse.net4j.internal.tcp.TCPSelector();
    selector.activate();

    // Prepare connector
    org.eclipse.net4j.internal.tcp.TCPClientConnector connector = new org.eclipse.net4j.internal.tcp.TCPClientConnector();
    connector.getConfig().setBufferProvider(bufferProvider);
    connector.getConfig().setReceiveExecutor(receiveExecutor);
    connector.getConfig().setProtocolProvider(protocolProvider);
    connector.getConfig().setNegotiator(null);
    connector.setSelector(selector);
    connector.setHost("localhost"); //$NON-NLS-1$
    connector.setPort(2042);
    connector.activate();

    // Create configuration
    CDOSessionConfiguration configuration = CDONet4jUtil.createSessionConfiguration();
    configuration.setConnector(connector);
    configuration.setRepositoryName("marketplace1"); //$NON-NLS-1$

    CDOAuthenticator authenticator = configuration.getAuthenticator();

    authenticator.setCredentialsProvider(new IPasswordCredentialsProvider()
    {
      IPasswordCredentials credentials = new IPasswordCredentials()
      {
        public char[] getPassword()
        {
          return cdo_pass.toCharArray();
        }

        public String getUserID()
        {
          return cdo_user;
        }
      };

      public boolean isInteractive()
      {
        return false;
      }

      public IPasswordCredentials getCredentials()
      {
        return credentials;
      }
    });

    // Open session
    CDOSession session = configuration.openSession();
    session.getPackageRegistry().putEPackage(MarketplacePackage.eINSTANCE);
    CDOBranch branch = session.getBranchManager().getBranch("MAIN/draft");
    Assert.isNotNull(branch);
    CDOTransaction transaction = session.openTransaction(branch);
    CDOResource resource = transaction.getOrCreateResource(PARTICIPANTS);

    if (resource.getContents().size() == 0)
      throw new Exception("No marketplace model available");

    EntityType type = (EntityType) resource.getContents().get(0);

    if(type!=null){
      out.println("Model is loaded Successfully!");
    }
    
    // session.getPackageRegistry().putEPackage(CompanyPackage.eINSTANCE);
    //
    // // Open transaction
    // CDOTransaction transaction = session.openTransaction();
    //
    // // Get or create resource
    //    CDOResource resource = transaction.getOrCreateResource("/path/to/my/resource"); //$NON-NLS-1$
    //
    // // Work with the resource and commit the transaction
    // EObject object = CompanyFactory.eINSTANCE.createCompany();
    // resource.getContents().add(object);
    // transaction.commit();

    // Cleanup
    session.close();
    connector.deactivate();
  }
  
//  public static void main(String[] args) throws Exception
//  {
//    // Enable logging and tracing
//    OMPlatform.INSTANCE.setDebugging(true);
//    OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
//    OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);
//
//    // Prepare container
//    IManagedContainer container = ContainerUtil.createContainer();
//    Net4jUtil.prepareContainer(container); // Register Net4j factories
//    TCPUtil.prepareContainer(container); // Register TCP factories
//    CDONet4jUtil.prepareContainer(container); // Register CDO factories
//    container.activate();
//
//    // Create connector
//    IConnector connector = TCPUtil.getConnector(container, "localhost:2042"); //$NON-NLS-1$
//
//    // Create configuration
//    CDOSessionConfiguration configuration = CDONet4jUtil.createSessionConfiguration();
//    configuration.setConnector(connector);
//    configuration.setRepositoryName(cdo_repo);
//
//    // use CDO authentication
//    CDOAuthenticator authenticator = configuration
//        .getAuthenticator();
//
//    authenticator
//        .setCredentialsProvider(new IPasswordCredentialsProvider() {
//          IPasswordCredentials credentials = new IPasswordCredentials() {
//            @Override
//            public char[] getPassword() {
//              return cdo_pass.toCharArray();
//            }
//
//            @Override
//            public String getUserID() {
//              return cdo_user;
//            }
//          };
//
//          @Override
//          public boolean isInteractive() {
//            return false;
//          }
//
//          @Override
//          public IPasswordCredentials getCredentials() {
//            return credentials;
//          }
//        });
//
//    CDOSession session = configuration.openSession();
//    session.getPackageRegistry().putEPackage(MarketplacePackage.eINSTANCE);
//
//    CDOBranch branch = session.getBranchManager().getBranch("MAIN/draft");
//    Assert.isNotNull(branch);
//    CDOTransaction transaction = session.openTransaction(branch);
//
////  ResourceSet resourceSet = transaction.getResourceSet();
////  URI resourceURI =
////    CDOURIUtil.createResourceURI(transaction, PARTICIPANTS);
////  Resource resource = resourceSet.getResource(resourceURI, true);
//  
////    CDOResource resource = transaction.getOrCreateResource(MARKETPLACE);
//    CDOResource resource = transaction.getOrCreateResource(PARTICIPANTS);
//
//    if (resource.getContents().size() == 0)
//      throw new Exception("No marketplace model available");
//
//    EntityType obj = (EntityType) resource.getContents().get(0);
//
//  }
}
