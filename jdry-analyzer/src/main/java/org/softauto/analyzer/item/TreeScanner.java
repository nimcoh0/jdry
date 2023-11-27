package org.softauto.analyzer.item;


import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.base.tree.BaseScanner;
import org.softauto.analyzer.base.tree.BaseTree;
import org.softauto.analyzer.core.dao.api.ApiDataProvider;
import org.softauto.core.Analyze;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;
import java.util.List;


public class TreeScanner {

    private static Logger logger = LogManager.getLogger(TreeScanner.class);

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
        logger.debug("number of trees to analyze  " + trees.size());
        for (GenericItem tree : trees) {
            logger.debug(" ----------- analyze  tree " + tree.getName() + " -------------------");
            if (tree.getType().equals("method"))
                new TreeImpl().walkOnTree(tree, new TreeVisitor() {

                    @Override
                    public Item visitBase(GenericItem tree) {
                        logger.debug(" ***** analyze  meta phase for " + tree.getName() + " ***** ");
                        return (Item)  new BaseTree.Item().accept(new BaseScanner(), tree, null, null);
                    }
                });
            }
        analyze.setSteps(trees);

        }
    }
