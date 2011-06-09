/**
 * Copyright (c) 2003-2011 Tradescape Corporation.
 * All rights reserved.
 */
package tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;

import biz.tradescape.mmt.model.marketplace.Entity;
import biz.tradescape.mmt.model.marketplace.EntityKeyDescriptor;
import biz.tradescape.mmt.model.marketplace.EntityMapEntryValue;
import biz.tradescape.mmt.model.marketplace.ParameterBlock;
import biz.tradescape.mmt.model.marketplace.ParameterBlockContainer;
import biz.tradescape.mmt.model.marketplace.ParameterMap;
import biz.tradescape.mmt.model.marketplace.ParameterMapContainer;
import biz.tradescape.mmt.model.marketplace.ParameterSet;
import biz.tradescape.mmt.model.marketplace.PrimaryContract;
import biz.tradescape.mmt.model.marketplace.PrimaryContractKeyDescriptor;
import biz.tradescape.mmt.model.marketplace.PrimaryContractMapEntryValue;
import biz.tradescape.mmt.model.marketplace.SubContract;
import biz.tradescape.mmt.model.marketplace.ValidityPeriod;
import biz.tradescape.mmt.model.marketplace.helper.LookupKey;
import biz.tradescape.mmt.model.marketplace.impl.PrimaryContractMapEntryImpl;

/**
 * @author chengdong
 *
 */
public class ValidityPeriodValidatorTest implements EValidator
{
    public static final ValidityPeriodValidatorTest INSTANCE = new ValidityPeriodValidatorTest();

    public static final String DIAGNOSTIC_SOURCE = "biz.tradescape.mmt.model.marketplace";

    public static final int PARAMETER_BLOCK_CAT = 1;
    public static final int PARAMETER_BLOCK__START_IN_RANGE = 2;

    public static final int SUB_CONTRACT_CAT = 10;
    public static final int SUB_CONTRACT__START_IN_RANGE = 11;
    public static final int SUB_CONTRACT__END_IN_RANGE = 12;
    public static final int SUB_CONTRACT__RANGE = 13; // RANGE_OVERLAP

    public static final int PARAMETER_MAP_CAT = 20;
    
    public static final int LOOKUP_KEY_CAT = 30;
    public static final int LOOKUP_KEY__START_IN_RANGE = 31;
    public static final int LOOKUP_KEY__END_IN_RANGE = 32;
    public static final int LOOKUP_KEY__RANGE = 33;

    public static final int LOOKUP_KEY_ENTRY_CAT = 40;
    public static final int LOOKUP_KEY_ENTRY__RANGE = 43; // RANGE_OVERLAP
    public static final int LOOKUP_KEY_ENTRY__VALUE = 44; // RANGE_OVERLAP

    public static final int PRIMARY_CONTRACT_CAT = 50;

    public static final int ENTITY_CAT = 60;

    public static final String SOURCE_ENTITY = "Participant";
    public static final String SOURCE_PRIMARY_CONTRACT="Primary Contract";
    public static final String SOURCE_SUB_CONTRACT="Sub Contract";
    public static final String SOURCE_PARAMETER_MAP="Parameter Map";
    public static final String SOURCE_PARAMETER_SET="Parameter Map Entry";//"ParameterSet";
    public static final String SOURCE_PARAMETER_BLOCK="Parameter";//"ParameterBlock";
    public static final String SOURCE_PRIMARY_CONTRACT_MAP_ENTRY_VALUE="Lookup Key";//"PrimaryContractMapEntryValue";
    public static final String SOURCE_PRIMARY_CONTRACT_MAP_ENTRY = "Lookup Key Entry";//"PrimaryContractMapEntry";
    public static final String SOURCE_ENTITY_MAP_ENTRY_VALUE="Lookup Key";//"PrimaryContractMapEntryValue";
    public static final String SOURCE_ENTITY_MAP_ENTRY = "Lookup Key Entry";
    
    // context properties and possible values you can pass
    public static final String CONTEXT__PRIMARY_CONTRACT="context.primaryContractOption";
    public static final int VALUE__PRIMAY_CONTRACT_SKIP_LOOKUP_KEY=1;
    public static final int VALUE__PRIMAY_CONTRACT_LOOKUP_KEY_ONLY=2;


    
    public static String getDisplayString(ValidityPeriod vp){
      return vp.getStart().getDisplayString()+","+vp.getEnd().getDisplayString(); 
    }
    
    @Override
    public boolean validate(EDataType eDataType, Object value,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      return false;
    }

    @Override
    public boolean validate(EObject eObject, DiagnosticChain diagnostics,
      Map<Object, Object> context)
    {
      return validate(eObject.eClass(), eObject, diagnostics, context);
    }

    @Override
    public boolean validate(EClass eClass, EObject eObject,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      if(eObject instanceof Entity){
        return validateEntity((Entity)eObject,diagnostics,context);
      }else if(eObject instanceof PrimaryContract){
        return validatePrimaryContract((PrimaryContract)eObject,diagnostics,context);
      }else if(eObject instanceof SubContract){
        return validateSubContract((SubContract)eObject,diagnostics,context);
      }else if(eObject instanceof ParameterMap){
        return validateParameterMap((ParameterMap)eObject,diagnostics,context);
      }
      return true;
    }
    
    public BasicDiagnostic validate(EObject eObject){
      Map<Object, Object> context=new HashMap<Object, Object>();
      return validate(eObject,context);
    }
    
    public BasicDiagnostic validate(EObject eObject, Map<Object,Object> context){
      BasicDiagnostic diagnostics = new BasicDiagnostic(Diagnostic.OK,
        eObject.eClass().getName(),//DIAGNOSTIC_SOURCE,
        0,
        "",
        new Object[]{eObject});

      validate(eObject, diagnostics, context);
      return diagnostics;
    }

    protected boolean validateEntity(Entity entity,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      boolean nonLookupKeysValid=true;
      boolean lookupKeysValid=true;

      nonLookupKeysValid=validateEntityWithoutLookupKey(entity,diagnostics,context);
      lookupKeysValid=validateEntityWithLookupKeyOnly(entity,diagnostics,context);
      
      return nonLookupKeysValid && lookupKeysValid;
    }

    private boolean validateEntityWithLookupKeyOnly(Entity entity,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      ValidityPeriod newPeriod=entity.getValidityPeriod();
      
      boolean result=true;
      EList<EntityMapEntryValue> entityMapEntryValues = entity.getMapEntryValues();
      /*
       *  multiple key maps to same contract
       *   - verify all VPs are bounded by new VP
       */
      // multiple keys map to the same contract at different VP
      //   - all VP of entry values should be bounded by new VP
      for(EntityMapEntryValue mapEntryValue:entityMapEntryValues){
        ValidityPeriod oldVP = mapEntryValue.getValidityPeriod();
        if(mapEntryValue.getEntity()!=entity){
          result &= false;
          diagnostics.add
          (new BasicDiagnostic(Diagnostic.ERROR,
            SOURCE_ENTITY_MAP_ENTRY_VALUE,
             LOOKUP_KEY_ENTRY__VALUE,
             String.format("%s [%s] '%s' does not match '%s'",
               mapEntryValue.getLookupKey().toString(),
               "Value", mapEntryValue.getEntity().getName(),entity.getName()),
             new Object[]{mapEntryValue}));
        }
        
        if(entityMapEntryValues.size()==1){
          if(!newPeriod.getStart().isEquals(oldVP.getStart())
            ||!newPeriod.getEnd().isEquals(oldVP.getEnd())){
            result &= false;
            diagnostics.add
            (new BasicDiagnostic(Diagnostic.ERROR,
              SOURCE_ENTITY_MAP_ENTRY_VALUE,
               LOOKUP_KEY__RANGE,
               String.format("%s [%s] '%s' does not match '%s'",
                 mapEntryValue.getLookupKey().toString(),
                 "Range", getDisplayString(oldVP),getDisplayString(newPeriod)),
               new Object[]{mapEntryValue}));
          }
        }else if(!newPeriod.contains(oldVP)){
          result &= false;
          diagnostics.add
          (new BasicDiagnostic(Diagnostic.ERROR,
            SOURCE_ENTITY_MAP_ENTRY_VALUE,
             LOOKUP_KEY__RANGE,
             String.format("%s [%s] '%s' is out of range",
               mapEntryValue.getLookupKey().toString(),
               "Range", getDisplayString(oldVP)),
             new Object[]{mapEntryValue}));
        }
     }
      
     return result;
   }

    private boolean validateEntityWithoutLookupKey(Entity entity,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
//      ValidityPeriod newPeriod=entity.getValidityPeriod();

      BasicDiagnostic parameterBlocksDiagnostics = new BasicDiagnostic(Diagnostic.OK,
        SOURCE_ENTITY,//primContract.eClass().getName(),
        PARAMETER_BLOCK_CAT,
        "",
        new Object[]{entity});
      
      boolean parameterBlocksValid = validateParameterBlocks(entity, parameterBlocksDiagnostics, context);
      if(!parameterBlocksValid)
        diagnostics.add(parameterBlocksDiagnostics);
      
      BasicDiagnostic primContractsDiagnostics = new BasicDiagnostic(Diagnostic.OK,
        SOURCE_ENTITY,//DIAGNOSTIC_SOURCE,
        PRIMARY_CONTRACT_CAT,
        "",
        new Object[]{entity});
      boolean primContractsValid = validatePrimaryContracts(entity, primContractsDiagnostics, context);
      if(!primContractsValid)
        diagnostics.add(primContractsDiagnostics);

      BasicDiagnostic parameterMapsDiagnostics = new BasicDiagnostic(Diagnostic.OK,
        SOURCE_ENTITY,
        PARAMETER_MAP_CAT,
        "",
        new Object[]{entity});
      boolean parameterMapsValid = validateParameterMaps(entity, parameterMapsDiagnostics, context);
      if(!parameterMapsValid)
        diagnostics.add(parameterMapsDiagnostics);
      
      return parameterBlocksValid && parameterMapsValid && primContractsValid;
    }

    protected boolean validatePrimaryContract(PrimaryContract primContract,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      Integer option=(Integer) context.get(CONTEXT__PRIMARY_CONTRACT);

      boolean nonLookupKeysValid=true;
      boolean lookupKeysValid=true;

      if(option==null){
        nonLookupKeysValid=validatePrimaryContractWithoutLookupKey(primContract,diagnostics,context);
        lookupKeysValid=validatePrimaryContractWithLookupKeyOnly(primContract,diagnostics,context);
      }else if(VALUE__PRIMAY_CONTRACT_SKIP_LOOKUP_KEY==option){
        nonLookupKeysValid=validatePrimaryContractWithoutLookupKey(primContract,diagnostics,context);
      }else if(VALUE__PRIMAY_CONTRACT_LOOKUP_KEY_ONLY==option){
        lookupKeysValid=validatePrimaryContractWithLookupKeyOnly(primContract,diagnostics,context);
      }
      
      return nonLookupKeysValid && lookupKeysValid;
    }
    
    protected boolean validatePrimaryContractWithLookupKeyOnly(
      PrimaryContract primContract, DiagnosticChain diagnostics,
      Map<Object, Object> context)
    {
      BasicDiagnostic lookupKeysDiagnostics = new BasicDiagnostic(Diagnostic.OK,
        SOURCE_PRIMARY_CONTRACT,
        LOOKUP_KEY_CAT,
        "",
        new Object[]{primContract});
      boolean lookupKeysValid = validatePrimaryContractLookupKeys(primContract, lookupKeysDiagnostics, context);
      if(!lookupKeysValid)
        diagnostics.add(lookupKeysDiagnostics);

      return lookupKeysValid;
    }

    protected boolean validatePrimaryContractWithoutLookupKey(
      PrimaryContract primContract, DiagnosticChain diagnostics,
      Map<Object, Object> context)
    {
      BasicDiagnostic parameterBlocksDiagnostics = new BasicDiagnostic(Diagnostic.OK,
        SOURCE_PRIMARY_CONTRACT,//primContract.eClass().getName(),
        PARAMETER_BLOCK_CAT,
        "",
        new Object[]{primContract});
      boolean parameterBlocksValid = validateParameterBlocks(primContract, parameterBlocksDiagnostics, context);
      if(!parameterBlocksValid)
        diagnostics.add(parameterBlocksDiagnostics);
      
      BasicDiagnostic subContractsDiagnostics = new BasicDiagnostic(Diagnostic.OK,
        SOURCE_PRIMARY_CONTRACT,//DIAGNOSTIC_SOURCE,
        SUB_CONTRACT_CAT,
        "",
        new Object[]{primContract});
      boolean subContractsValid = validateSubContracts(primContract, subContractsDiagnostics, context);
      if(!subContractsValid)
        diagnostics.add(subContractsDiagnostics);
      
      BasicDiagnostic parameterMapsDiagnostics = new BasicDiagnostic(Diagnostic.OK,
        SOURCE_PRIMARY_CONTRACT,
        PARAMETER_MAP_CAT,
        "",
        new Object[]{primContract});
      boolean parameterMapsValid = validateParameterMaps(primContract, parameterMapsDiagnostics, context);
      if(!parameterMapsValid)
        diagnostics.add(parameterMapsDiagnostics);
      
      return parameterBlocksValid && subContractsValid && parameterMapsValid;
    }

    /**
     * All parameter block (except the first) should have a start within parent VP.
     * All parameter maps should have parameter map entry's start within parent VP.
     * 
     * @param subContract
     * @param diagnostics
     * @param context
     * @return
     */
    protected boolean validateSubContract(SubContract subContract,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      ValidityPeriod newPeriod = subContract.getParentContract().getValidityPeriod();
      
      BasicDiagnostic parameterBlocksDiagnostics = new BasicDiagnostic(Diagnostic.OK,
        SOURCE_SUB_CONTRACT,
        PARAMETER_BLOCK_CAT,
        "",
        new Object[]{subContract});
      boolean parameterBlocksValid = validateParameterBlocks(subContract, parameterBlocksDiagnostics, context);
      if(!parameterBlocksValid)
        diagnostics.add(parameterBlocksDiagnostics);

      BasicDiagnostic parameterMapsDiagnostics = new BasicDiagnostic(Diagnostic.OK,
        SOURCE_SUB_CONTRACT,
        PARAMETER_MAP_CAT,
        "",
        new Object[]{subContract});
      boolean parameterMapsValid = validateParameterMaps(subContract, parameterMapsDiagnostics, context);
      if(!parameterMapsValid)
        diagnostics.add(parameterMapsDiagnostics);
      
      boolean parentValid = true;
      if(!subContract.getPrimaryContract().getValidityPeriod().contains(newPeriod)){
        parentValid &= false;
        diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
          SOURCE_SUB_CONTRACT, SUB_CONTRACT__RANGE, String.format(
            "[%s] '%s' conflicts with parent '%s'", "Range",
            getDisplayString(newPeriod), getDisplayString(subContract
              .getPrimaryContract().getValidityPeriod())), new Object[]{subContract.getPrimaryContract()}));
      }
      
      return parameterBlocksValid && parameterMapsValid && parentValid;
    }

    protected boolean validateParameterMap(ParameterMap map,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      boolean result=true;

      BasicDiagnostic defaultParameterDiagnostics = new BasicDiagnostic(Diagnostic.OK,
              SOURCE_PARAMETER_MAP,
              PARAMETER_BLOCK_CAT,
              "",
              new Object[]{map});
	    boolean defaultParameterValid = validateParameterBlocks(map, defaultParameterDiagnostics, context);
	    if(!defaultParameterValid){
	      diagnostics.add(defaultParameterDiagnostics);
	    }
	    result &= defaultParameterValid;

      for(Entry<LookupKey, ParameterSet> entry:map.getHashMap()){
        ParameterSet ps = entry.getValue();

        BasicDiagnostic parameterBlocksDiagnostics = new BasicDiagnostic(Diagnostic.OK,
          SOURCE_PARAMETER_SET,
          PARAMETER_BLOCK_CAT,
          "",
          new Object[]{ps});
        boolean parameterBlocksValid = validateParameterBlocks(ps, parameterBlocksDiagnostics, context);
        if(!parameterBlocksValid){
          diagnostics.add(parameterBlocksDiagnostics);
        }
        result &= parameterBlocksValid;
      }
      return result;
    }

    protected boolean validateParameterMaps(ParameterMapContainer container,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      boolean result=true;
      for(ParameterMap map: container.getParameterMaps()){
        BasicDiagnostic parameterMapDiagnostics = new BasicDiagnostic(Diagnostic.OK,
          SOURCE_PARAMETER_MAP,
          PARAMETER_MAP_CAT,
          "",
          new Object[]{map});
        boolean parameterMapValid = validateParameterMap(map, parameterMapDiagnostics, context);
        if(!parameterMapValid){
          diagnostics.add(parameterMapDiagnostics);
        }
        result &= parameterMapValid;
      }
      return result;
    }
    

    /**
     * All primary contracts should be bounded by the new Period.
     * Primary contracts VP is not necessary contained by or covers container's VP.
     * @param entity
     * @param diagnostics
     * @param context
     * @param newPeriod
     * @return
     */
    protected boolean validatePrimaryContracts(Entity entity,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      boolean result=true;
      EList<PrimaryContract> primContracts = entity.getPrimaryContracts();
      for(int i=0;i<primContracts.size();i++){
        PrimaryContract primContract = primContracts.get(i);
        BasicDiagnostic primContractDiagnostics = new BasicDiagnostic(Diagnostic.OK,
          SOURCE_PRIMARY_CONTRACT,
          PRIMARY_CONTRACT_CAT,
          "",
          new Object[]{primContract});
        boolean primContractValid = validatePrimaryContract(primContract, primContractDiagnostics, context);
        if(!primContractValid){
          diagnostics.add(primContractDiagnostics);
        }
        result &= primContractValid;
      }
      return result;
    }   

    /**
     * All sub contracts should be bounded by the new Period.
     * Sub contracts VP is not necessary covers container's VP.
     *<pre>
     *e.g.
     *       pc: 2000-01-01 ~ 2010-01-01
     *           pb: 2000-01-01
     *               2002-01-01
     *           sc: 2002-01-01 ~ 2004-01-01
     *               pm:2002-01-01
     *               2003-01-01
     *           sc: 2006-01-01 ~ 2008-01-01
     *               pm:2006-01-01
     *               2007-01-01
     *</pre>
     *If pc change to: 2002-01-02 ~ 2007-12-31, there will be sub 
     *contract confilcts.
     *
     * @param primContract
     * @param diagnostics
     * @param context
     * @return
     */
    protected boolean validateSubContracts(PrimaryContract primContract,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      ValidityPeriod newPeriod = primContract.getValidityPeriod();
      
      boolean result=true;
      EList<SubContract> subContracts = primContract.getSubContracts();
      for(int i=0;i<subContracts.size();i++){
        SubContract subContract = subContracts.get(i);
        ValidityPeriod oldVP = subContract.getValidityPeriod();
        if(!newPeriod.contains(oldVP)){
          result &= false;
          diagnostics.add
          (new BasicDiagnostic(Diagnostic.ERROR,
            SOURCE_SUB_CONTRACT,
             SUB_CONTRACT__RANGE,
             String.format("[%s] %s '%s' is out of range","Range",
               subContract.getName(),getDisplayString(oldVP)),
             new Object[]{subContract}));
        }
        
        BasicDiagnostic subContractDiagnostics = new BasicDiagnostic(Diagnostic.OK,
          SOURCE_SUB_CONTRACT,
          SUB_CONTRACT_CAT,
          "",
          new Object[]{subContract});
        boolean subContractValid = validateSubContract(subContract, subContractDiagnostics, context);
        if(!subContractValid){
          diagnostics.add(subContractDiagnostics);
        }
        result &= subContractValid;
      }
      return result;
    }

    /**
     * Iterate the lookup key map entry descriptors, and verify lookup entry
     * in each table (while considering the auto adjustment) is bounded by 
     * newPeriod.
     * <p/>
     * First check if need auto adjust or not. If no adjust needed, OK
     * Second check if adjustable or not:
     *   after adjustment, all VP are bounded by the new VP
     *   after adjustment, no overlap between the adjusted VPs and
     *   existent VP
     * <p/>
     * <ul>As regard to Auto adjust: 
     *   <li>the first map entry value's start should be adjusted to the new VP's start.</li>
     *   <li>the last map entry value's end should be adjusted to the new VP's end.</li>
     * </ul>
     * <pre>
     * The lookup key table can be as complex as this:
     *     Key      start        end         Contract
     *     k1       01/01/2000   12/31/2002  c1
     *              01/01/2003   12/31/2004  c2
     *              01/01/2005   12/31/2006  c3
     *     k2       01/01/2010   12/31/2012  c1
     *              01/01/2013   12/31/2014  c2
     *              01/01/2015   12/31/2016  c3
     *     k3       01/01/2020   12/31/2022  c1
     *              01/01/2023   12/31/2024  c2
     *              01/01/2025   12/31/2026  c3
     * The Lookup key map values of c1 looks like this:
     *     Contract  KeyMapValues
     *     c1        (k1,k2,k3)
     *     c2        (k1,k2,k3)
     *     c3        (k1,k2,k3)
     * 
     * Validations on change of c1 VP
     *   Case  Action      NewVP                    Conflicts
     *   A     tight       01/01/2001~12/31/2021    valid
     *   B     relax       01/01/1999~12/31/2023    valid
     *   C     tight       01/01/2001~01/01/2004    same contract VP conflicts (multiple keys to same contract)
     *   D                 01/01/2001~01/01/2004    c1,c2 - same key maps to different contracts
     *   
     * Case C:
     *     Key      start        end         Contract
     *     k1       01/01/2000   12/31/2002  c1
     *     k2       01/01/2010   12/31/2012  c1
     *     k3       01/01/2020   12/31/2022  c1
     *     
     * Case D:
     *     Key      start        end         Contract
     *     k1       01/01/2000   12/31/2002  c1
     *              01/01/2003   12/31/2004  c2
     *              01/01/2005   12/31/2006  c3
     * </pre>
     */
    protected boolean validatePrimaryContractLookupKeys(PrimaryContract primContract,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      ValidityPeriod newPeriod = primContract.getValidityPeriod();
      
      boolean result=true;
      EList<PrimaryContractMapEntryValue> contractMapEntryValues = primContract.getMapEntryValues();
      /*
       *  multiple key maps to same contract
       *   - verify all VPs are bounded by new VP
       */
      // multiple keys map to the same contract at different VP
      //   - all VP of entry values should be bounded by new VP
      for(PrimaryContractMapEntryValue mapEntryValue:contractMapEntryValues){
        ValidityPeriod oldVP = mapEntryValue.getValidityPeriod();
        
        if(mapEntryValue.getPrimaryContract()!=primContract){
          result &= false;
          diagnostics.add
          (new BasicDiagnostic(Diagnostic.ERROR,
            SOURCE_PRIMARY_CONTRACT_MAP_ENTRY_VALUE,
             LOOKUP_KEY_ENTRY__VALUE,
             String.format("%s [%s] '%s' does not match '%s'",
               mapEntryValue.getLookupKey().toString(),
               "Value", mapEntryValue.getPrimaryContract().getName(),primContract.getName()),
             new Object[]{mapEntryValue}));
        }

        if(contractMapEntryValues.size()==1){
          if(!newPeriod.getStart().isEquals(oldVP.getStart())
            ||!newPeriod.getEnd().isEquals(oldVP.getEnd())){
            result &= false;
            diagnostics.add
            (new BasicDiagnostic(Diagnostic.ERROR,
              SOURCE_PRIMARY_CONTRACT_MAP_ENTRY_VALUE,
               LOOKUP_KEY__RANGE,
               String.format("%s [%s] '%s' does not match '%s'",
                 mapEntryValue.getLookupKey().toString(),
                 "Range", getDisplayString(oldVP),getDisplayString(newPeriod)),
               new Object[]{mapEntryValue}));
          }
        }else if(!newPeriod.contains(oldVP)){
          result &= false;
          diagnostics.add
          (new BasicDiagnostic(Diagnostic.ERROR,
            SOURCE_PRIMARY_CONTRACT_MAP_ENTRY_VALUE,
             LOOKUP_KEY__RANGE,
             String.format("%s [%s] '%s' is out of range",
               mapEntryValue.getLookupKey().toString(),
               "Range", getDisplayString(oldVP)),
             new Object[]{mapEntryValue}));
        }
     }
      
     return result;
    }

    protected boolean validateLookupKeyMapEntry(PrimaryContractMapEntryImpl entry,
      BasicDiagnostic diagnostics, Map<Object, Object> context,
      ValidityPeriod newPeriod, PrimaryContractMapEntryValue exlusive)
    {
      boolean result=true;
      
      for(Entry<LookupKey, EList<PrimaryContractMapEntryValue>> ent: entry.getEMap()){
        if(ent.getKey().equals(entry.getKey()) && ent!=entry){
          for(PrimaryContractMapEntryValue val: ent.getValue()){
            if(val!=exlusive && val.getValidityPeriod().intersects(newPeriod)){
              result &= false;
              diagnostics.add
              (new BasicDiagnostic(Diagnostic.ERROR,
                 SOURCE_PRIMARY_CONTRACT_MAP_ENTRY_VALUE,//val.eClass().getName(),
                 LOOKUP_KEY_ENTRY__RANGE,
                 String.format("[%s] '%s' and '%s' overlaps","Range",
                   getDisplayString(val.getValidityPeriod()),
                   getDisplayString(newPeriod)),
                 new Object[]{val}));
            }
          }
        }
      }
      return result;
    }

    public PrimaryContractMapEntryValue find(
      PrimaryContractKeyDescriptor keyDesc, LookupKey key, 
      ValidityPeriod validityPeriod, List<PrimaryContractMapEntryValue> filterSet)
    {
      if (key == null || validityPeriod == null) {
        return null;
      }
      EMap<LookupKey, EList<PrimaryContractMapEntryValue>> map = keyDesc.getContractMap();
      EList<PrimaryContractMapEntryValue> values = map.get(key);
      if (values != null)
      {
        for (PrimaryContractMapEntryValue value : values)
        {
          if (value.intersects(validityPeriod) && !filterSet.contains(value)) {
            return value;
          }
        }
      }
      return null;
    }
    
    public EntityMapEntryValue find(
      EntityKeyDescriptor keyDesc, LookupKey key, 
      ValidityPeriod validityPeriod, List<EntityMapEntryValue> filterSet)
    {
      if (key == null || validityPeriod == null) {
        return null;
      }
      EMap<LookupKey, EList<EntityMapEntryValue>> map = keyDesc.getEntityMap();
      EList<EntityMapEntryValue> values = map.get(key);
      if (values != null)
      {
        for (EntityMapEntryValue value : values)
        {
          if (value.intersects(validityPeriod) && !filterSet.contains(value)) {
            return value;
          }
        }
      }
      return null;
    }
    
    /**
     * When copy primary contract, all children's vp will be auto adjusted,
     * we need to make sure all parameter blocks are bounded by the new period.
     */
    protected boolean validateParameterBlocks(ParameterBlockContainer container,
      DiagnosticChain diagnostics, Map<Object, Object> context)
    {
      ValidityPeriod newPeriod = container.getValidityPeriod();
      
      boolean result=true;
      EList<ParameterBlock> blocks = container.getParameterBlocks();
      for(int i=0;i<blocks.size();i++){
        ParameterBlock pb=blocks.get(i);
        if(!newPeriod.contains(pb.getStart())){
          result &= false;
          if (diagnostics != null)
          {
            diagnostics.add
            (new BasicDiagnostic(Diagnostic.ERROR,
              SOURCE_PARAMETER_BLOCK,
              PARAMETER_BLOCK__START_IN_RANGE,
              String.format("[Start] '%s' is out of range '%s'",pb.getStart().getDisplayString(),getDisplayString(newPeriod)),
              new Object[]{pb}));
          }
        }
      }
      return result;
    }
    
}
