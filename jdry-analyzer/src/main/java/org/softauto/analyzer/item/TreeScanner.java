package org.softauto.analyzer.item;


import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.softauto.analyzer.base.tree.BaseScanner;
import org.softauto.analyzer.base.tree.BaseTree;
import org.softauto.analyzer.core.dao.api.ApiDataProvider;
import org.softauto.core.Analyze;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;

import java.util.ArrayList;
import java.util.List;


public class TreeScanner {

    private static Logger logger = LogManager.getLogger(TreeScanner.class);

    private static final Marker JDRY = MarkerManager.getMarker("JDRY");

    List<GenericItem> trees;

    Analyze analyze = new Analyze();

    public Analyze getAnalyze() {
        return analyze;
    }

    public List<GenericItem> getTrees() {
        return trees;
    }

    public TreeScanner() {
        ApiDataProvider apiDataProvider = ApiDataProvider.getInstance().initialize();
        JsonNode json = ApiDataProvider.getInstance().getParser().getJson();
        analyze.setName(json.get("name").asText());
        analyze.setNamespace(json.get("namespace").asText());
        analyze.setDoc(json.get("doc").asText());
        analyze.setVersion(json.get("version").asText());
        trees = apiDataProvider.getTrees();
        List<GenericItem> steps = new ArrayList<>();
        List<GenericItem> listeners = new ArrayList<>();
        logger.debug(JDRY,"number of trees to analyze  " + trees.size());
        for (GenericItem tree : trees) {
            logger.debug(JDRY," ----------- analyze  tree " + tree.getName() + " -------------------");
            if (tree.getType().equals("method") || tree.getType().equals("listener"))
                new TreeImpl().walkOnTree(tree, new TreeVisitor() {

                    @Override
                    public Item visitBase(GenericItem tree) {
                        logger.debug(JDRY," ***** analyze  meta phase for " + tree.getName() + " ***** ");
                        new BaseTree.Item().accept(new BaseScanner(), tree, null, null);
                        if(tree.getType().equals("method")){
                            steps.add(tree);
                        }
                        if(tree.getType().equals("listener")){
                            listeners.add(tree);
                        }
                        return null;
                    }
                });
            }
        analyze.setSteps(steps);
        analyze.setListeners(listeners);
        }
    }
