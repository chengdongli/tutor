/**
 * http://www.eclipsezone.com/eclipse/forums/t111170.html
 * 
 * CVS: /org.eclipse.emf.cdo.examples/src/org/eclipse/emf/cdo/examples/StandaloneContainerExample.java
 */
package tests;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.common.protocol.CDOAuthenticator;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.net4j.CDOSessionConfiguration;
import org.eclipse.emf.cdo.session.CDOCollectionLoadingPolicy;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CDOUtil;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;
import org.eclipse.net4j.util.security.IPasswordCredentials;
import org.eclipse.net4j.util.security.IPasswordCredentialsProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import biz.tradescape.mmt.EObjectPrintUtil;
import biz.tradescape.mmt.model.marketplace.Entity;
import biz.tradescape.mmt.model.marketplace.EntityType;
import biz.tradescape.mmt.model.marketplace.MarketplacePackage;

/**
 * @author chengdong
 *
 */
public class CollectionLoadingTest
{
  final static String cdo_server = "tcp://localhost:2042/?repositoryName=marketplace1";
  final static String cdo_user = "anonymous";
  final static String cdo_pass = "passw";
  final static String cdo_repo = "marketplace1";
  final static String cdo_branch="MAIN/draft";
  final static String cdo_resource__participants="participants";
  final static String cdo_resource__marketplace = "marketplace";

  private CDOSession session;
  private IManagedContainer container;
  private IConnector connector;
  
  @Before
  public void setup() throws Exception{

    // eanble trace
//    enableTrace();
    
    // Prepare container
    container = ContainerUtil.createContainer();
    Net4jUtil.prepareContainer(container); // Register Net4j factories
    TCPUtil.prepareContainer(container); // Register TCP factories
    CDONet4jUtil.prepareContainer(container); // Register CDO factories
    container.activate();

    // Create connector
    connector = TCPUtil.getConnector(container, "localhost:2042"); //$NON-NLS-1$

    // Create configuration
    CDOSessionConfiguration configuration = CDONet4jUtil.createSessionConfiguration();
    configuration.setConnector(connector);
    configuration.setRepositoryName(cdo_repo);

    // use CDO authentication
    CDOAuthenticator authenticator = configuration
        .getAuthenticator();

    authenticator
        .setCredentialsProvider(new IPasswordCredentialsProvider() {
          IPasswordCredentials credentials = new IPasswordCredentials() {
            @Override
            public char[] getPassword() {
              return cdo_pass.toCharArray();
            }

            @Override
            public String getUserID() {
              return cdo_user;
            }
          };

          @Override
          public boolean isInteractive() {
            return false;
          }

          @Override
          public IPasswordCredentials getCredentials() {
            return credentials;
          }
        });

    session = configuration.openSession();
    session.getPackageRegistry().putEPackage(MarketplacePackage.eINSTANCE);

  }
  
  protected void enableTrace()
  {
    // Enable logging and tracing
    OMPlatform.INSTANCE.setDebugging(true);
    OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
    OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);
  }

  private void setPolicy(CDOSession _session,int initialChunkSize, int resolveChunkSize){
    CDOCollectionLoadingPolicy policy = CDOUtil.createCollectionLoadingPolicy(
      initialChunkSize, resolveChunkSize);
//      0, CDORevision.UNCHUNKED);
    _session.options().setCollectionLoadingPolicy(policy);

  }
  
  @After
  public void tearDown(){
    // Cleanup
    session.close();
    connector.close();
    container.deactivate();
    out.println("Session deactivated!");
  }
  
  @Test
  public void validateEntities() throws Exception{
    CDOBranch branch = session.getBranchManager().getBranch(cdo_branch);
    Assert.isNotNull(branch);
    CDOTransaction transaction = session.openTransaction(branch);

    int initialChunkSize=0;
    int resolveChunkSize=500;
    int entityNum=4069;
    setPolicy(session, initialChunkSize, resolveChunkSize);
    long nanoTime=System.nanoTime();
    CDOResource resource = transaction.getOrCreateResource(cdo_resource__participants);
    out.printf("Load Resource: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);
    if (resource.getContents().size() == 0)
      throw new Exception("No marketplace model available");
    
    EntityType type = (EntityType) resource.getContents().get(0);    
    out.printf("Load Type: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);
    if(type!=null){
      out.println("Model is loaded Successfully!");
    }
    
//    HelperEMF.cdoPrefetch(type, 1);
    EList<Entity> entities = type.getEntities();
    out.printf("Load Entities: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);
//    entities.get(2305);

    BasicDiagnostic diagnostics = new BasicDiagnostic(Diagnostic.OK,"model",0,"",new Object[0]);

    System.out.println("Validating start ....");
    for(int i=0;i<entities.size();i++){
      Entity entity = entities.get(i);
      System.out.println(String.format("Entity[%d]='%s'",i,entity.getName()));

//      Map<Object,Object> context=new HashMap<Object, Object>();
//      context.put(ValidityPeriodValidator.CONTEXT__PRIMARY_CONTRACT, ValidityPeriodValidator.VALUE__PRIMAY_CONTRACT_SKIP_LOOKUP_KEY);
      BasicDiagnostic diagnostic = ValidityPeriodValidatorTest.INSTANCE.validate(entity);
      diagnostics.add(diagnostic);
      
      diagnostic.recomputeSeverity();
      if(Diagnostic.OK!=diagnostic.getSeverity())
        System.out.println(EObjectPrintUtil.printDiagnostic(diagnostic));

//      for(PrimaryContract contract:entity.getPrimaryContracts()){
//        contract.getName();
//      }
    }

    System.out.println("Validating done !");

    diagnostics.recomputeSeverity();
//    if(Diagnostic.OK!=diagnostics.getSeverity())
//      EObjectPrintUtil.printDiagnostic(diagnostics);
    assertEquals(Diagnostic.OK, diagnostics.getSeverity());

    out.printf("Iterate Entities: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);

    transaction.close();
  }

//  @Test
  public void testLoadEntities1() throws Exception{
    CDOBranch branch = session.getBranchManager().getBranch(cdo_branch);
    Assert.isNotNull(branch);
    CDOTransaction transaction = session.openTransaction(branch);

    int initialChunkSize=0;
    int resolveChunkSize=500;
    int entityNum=4069;
    setPolicy(session, initialChunkSize, resolveChunkSize);
    long nanoTime=System.nanoTime();
    CDOResource resource = transaction.getOrCreateResource(cdo_resource__participants);
    out.printf("Load Resource: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);
    if (resource.getContents().size() == 0)
      throw new Exception("No marketplace model available");
    
    EntityType type = (EntityType) resource.getContents().get(0);    
    out.printf("Load Type: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);
    if(type!=null){
      out.println("Model is loaded Successfully!");
    }
    
//    HelperEMF.cdoPrefetch(type, 1);
    EList<Entity> entities = type.getEntities();
    out.printf("Load Entities: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);
//    entities.get(2305);
    for(int i=0;i<entities.size();i++){
      entities.get(i);
//      for(PrimaryContract contract:entity.getPrimaryContracts()){
//        contract.getName();
//      }
    }
    out.printf("Iterate Entities: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);

    transaction.close();
  }
 
//  @Test
  public void testLoadEntities2() throws Exception{
    CDOBranch branch = session.getBranchManager().getBranch("MAIN/draft");
    Assert.isNotNull(branch);
    CDOTransaction transaction = session.openTransaction(branch);

    int initialChunkSize=1000;
    int resolveChunkSize=CDORevision.UNCHUNKED;
    int entityNum=4069;
//    setPolicy(session, initialChunkSize, resolveChunkSize);
    long nanoTime=System.nanoTime();
    CDOResource resource = transaction.getOrCreateResource(cdo_resource__participants);
    out.printf("Load Resource: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);
    if (resource.getContents().size() == 0)
      throw new Exception("No marketplace model available");
    
    EntityType type = (EntityType) resource.getContents().get(0);    
    out.printf("Load Type: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);
    if(type!=null){
      out.println("Model is loaded Successfully!");
    }
    
    EList<Entity> entities = type.getEntities();
    out.printf("Load Entities: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);
    for(int i=0;i<entities.size();i++){
      entities.get(i);
    }
    out.printf("Iterate Entities: initialChunkSize=%d resolveChunkSize=%d entities=%d time(nano Secs)=%d\n",initialChunkSize,resolveChunkSize,entityNum,System.nanoTime()-nanoTime);

    transaction.close();
  }
  
//  @Test
  public void testLoadEntities3() throws Exception{
    CDOBranch branch = session.getBranchManager().getBranch("MAIN/draft");
    Assert.isNotNull(branch);
    CDOTransaction transaction = session.openTransaction(branch);

//    int initialChunkSize=CDORevision.UNCHUNKED;
//    int resolveChunkSize=CDORevision.UNCHUNKED;
//    setPolicy(session, initialChunkSize, resolveChunkSize);
    long nanoTime=System.nanoTime();
    int entityNum=4069;
    CDOResource resource = transaction.getOrCreateResource(cdo_resource__participants);
    out.printf("Load Resource: entities=%d time(nano Secs)=%d\n",entityNum,System.nanoTime()-nanoTime);
    if (resource.getContents().size() == 0)
      throw new Exception("No marketplace model available");
    
    EntityType type = (EntityType) resource.getContents().get(0);    
    if(type!=null){
      out.println("Model is loaded Successfully!");
    }
//    HelperEMF.cdoPrefetch(type, 1);
//    EList<Entity> entities = type.getEntities();
//    entities.get(2305);
//    for(Entity entity: entities){
//      for(PrimaryContract contract:entity.getPrimaryContracts()){
//        contract.getName();
//      }
//    }
    out.printf("Load Entities: entities=%d time(nano Secs)=%d\n",entityNum,System.nanoTime()-nanoTime);
    transaction.close();
  }

}
