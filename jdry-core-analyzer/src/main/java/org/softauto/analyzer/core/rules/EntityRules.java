package org.softauto.analyzer.core.rules;

import org.softauto.analyzer.core.utils.ApplyRule;

public class EntityRules {

/*
    public static String buildEntityName(String entityFullName){
        entityFullName =  Utils.getShortName(entityFullName);
        entityFullName =  NameRules.removeEntityPostfix(entityFullName);
        entityFullName = NameRules.removeEntityPrefix(entityFullName);
        return entityFullName;
    }


 */


/*
    public static  org.softauto.analyzer.model.entity.Entity getEntityIfExist(org.softauto.analyzer.model.entity.Entity entity){
        for(org.softauto.analyzer.model.entity.Entity entity1 : Suite.getEntities()){
            if(entity1.getClazz().equals(entity.getClazz()) && entity1.getName().equals(entity.getName()) ){
                return entity1;
            }
        }
        return null;
    }


 */

/*
    public static List<String> getEntityList(List<GenericItem> trees){
        List<String> entities = new ArrayList<>();
        for(GenericItem genericItem : trees){
            if(genericItem.getClassInfo().containsKey("entity")){
                if(genericItem.getClassInfo().get("entity").equals("true")){
                    entities.add(genericItem.getNamespce());
                }
            }
        }
        return entities;
    }


 */
    //public static Object overrideAfterReturnType(){
     //   return ApplyRule.setRule("override_after_return_type").apply().getResult();
    //}

    public static boolean isEntity(String type){
        return (boolean) ApplyRule.setRule("entity_identify").addContext("type", type).apply().getResult();
        /*
        if(Configuration.has("entity_identify")){
          String schema = Configuration.get("entity_identify").asString();
           return (boolean) Espl.getInstance().addProperty("argument",argument).evaluate(schema);
        }
        return false;

         */
    }

    public static String applyEntityNameFormat(String str){
        return ApplyRule.setRule("entity_name_format").addContext("name",str).apply().getResult();
    }

    public static String[] getCrudTypeForUnClassifyEntity(){
      return (String[]) ApplyRule.setRule("set_crud_type_for_un_classify_entity").apply().getResult();
    }

}
