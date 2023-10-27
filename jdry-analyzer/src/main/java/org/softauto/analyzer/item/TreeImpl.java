package org.softauto.analyzer.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.core.Main;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.listener.Listener;
import org.softauto.analyzer.model.suite.Suite;
import org.softauto.analyzer.model.test.Test;



public class TreeImpl implements Tree {

    private static Logger logger = LogManager.getLogger(Main.class);

    Suite suite;

    public TreeImpl(Suite suite){
        this.suite = suite;
    }

    @Override
    public TreeImpl walkOnTree(GenericItem tree, TreeVisitor visitor) {

        Item item = visitor.visitBase(tree);

        item = visitor.visitNameRecognition(item);

        item = visitor.visitJdryAnnotations(item);

        item = visitor.visitDataGenerator(item);

        item = visitor.visitDataRecorder(item);

        item = visitor.visitPublish(item);



        if (item instanceof Test) {
             suite.addTest((Test) item);
             logger.debug("test "+ ((Test) item).getFullName() + "add to suite");
        }else
             if (item instanceof Listener) {
                 suite.addListener((Listener) item);
                 logger.debug("Listener "+ ((Listener) item).getFullName() + "add to suite");
             }else{
                 logger.warn("Item was not add to suite "+ tree.getName());
             }
        return this;
    }



}
