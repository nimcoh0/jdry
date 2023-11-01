package org.softauto.analyzer.item;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.core.Main;
import org.softauto.analyzer.core.dao.api.ApiDataProvider;
import org.softauto.analyzer.core.rules.PublishRules;
import org.softauto.analyzer.core.system.config.Configuration;
import org.softauto.analyzer.core.system.config.Context;
import org.softauto.analyzer.core.system.plugin.ProviderManager;
import org.softauto.analyzer.core.system.plugin.api.Provider;
import org.softauto.analyzer.core.system.plugin.spi.PluginProvider;
import org.softauto.analyzer.core.utils.TreeTools;
import org.softauto.analyzer.core.utils.Utils;
import org.softauto.analyzer.model.Item;
import org.softauto.analyzer.model.genericItem.GenericItem;
import org.softauto.analyzer.model.suite.Suite;
import org.softauto.analyzer.model.test.Test;

import java.util.HashMap;
import java.util.List;


public class TreeScanner {

    private static Logger logger = LogManager.getLogger(Main.class);

    Suite suite = new Suite();

    public Suite getSuite() {
        return suite;
    }

    public TreeScanner() {
        suite.setName(Configuration.get(Context.SCHEMA_NAME).asString());
        suite.setNamespace(Configuration.get(Context.FILE_NAMESPACE).asString());
        //logger.debug("running TreeScanner for " + suite.getName());
        ApiDataProvider apiDataProvider = ApiDataProvider.getInstance().initialize();
        List<GenericItem> trees = apiDataProvider.getTrees();
        HashMap<String, String> entities = apiDataProvider.getEntities();
        //Espl.getInstance().addProperty("suite",suite);
        suite.setEntities(entities);
        TreeTools.setTrees(trees);
        TreeTools.setSuite(suite);
        logger.debug("number of trees to analyze  " + trees.size());
        for (GenericItem tree : trees) {
            logger.debug(" ----------- analyze  tree " + tree.getName() + " -------------------");
            if (tree.getType().equals("method"))
                new TreeImpl(suite).walkOnTree(tree, new TreeVisitor() {

                            @Override
                            public Item visitBase(GenericItem tree) {
                                logger.debug(" ***** analyze  meta phase for " + tree.getName() + " ***** ");
                                PluginProvider pluginProvider = ProviderManager.provider("Base");
                                if (pluginProvider != null) {
                                    Provider provider = pluginProvider.create();
                                    return (Item) provider.Analyze(tree, null, null);
                                }
                                return null;
                            }


                            @Override
                            public Item visitJdryAnnotations(Item item) {
                                logger.debug("***** analyze  Jdry Annotations phase for " + tree.getName() + "  ***** ");
                                PluginProvider pluginProvider = ProviderManager.provider("JdryAnnotations");
                                if (pluginProvider != null) {
                                    Provider provider = pluginProvider.create();
                                    return (Item) provider.Analyze(tree, item, null);
                                }
                                return item;
                            }



                    /*
                    @Override
                    public Item visitJdryAnnotations(Item item) {
                        logger.debug("***** analyze  Jdry Annotations phase for " + tree.getName() + "  ***** ");
                        PluginProvider pluginProvider = ProviderManager.provider("Annotations");
                        if (pluginProvider != null) {
                            Provider provider = pluginProvider.create();
                            return (Item) provider.Analyze(tree, item, null);
                        }
                        return item;
                    }


                    @Override
                    public Item visitDataGenerator(Item item) {
                        if (item instanceof Test) {
                            logger.debug("***** analyze  Data Generator phase for " + tree.getName() + " *****");
                            PluginProvider pluginProvider = ProviderManager.provider("Generator");
                            if (pluginProvider != null) {
                                Provider provider = pluginProvider.create();
                                return (Item) provider.Analyze(tree, item, null);
                            }
                        }
                        return item;
                    }


                    @Override
                    public Item visitDataRecorder(Item item) {
                        if (item instanceof Test) {
                            logger.debug("***** analyze  Data Recorder phase for  " + tree.getName() + " *****");
                            PluginProvider pluginProvider = ProviderManager.provider("Recorder");
                            if (pluginProvider != null) {
                                Provider provider = pluginProvider.create();
                                item = provider.Analyze(tree, item, null);
                            }
                        }
                        return item;
                    }

                    @Override
                    public Item visitPublish(Item item) {
                        logger.debug("publish test args and result");
                        if (item instanceof Test) {
                            Test test = (Test) item;
                            if (test.getExpected().getName() != null && !test.getExpected().getType().equals("void")) {
                                if (Utils.isPrimitive(test.getExpected().getType()) || !test.getExpected().getType().contains(Configuration.get(Context.DOMAIN).asString())) {
                                    test.addPublish(test.getResultPublishName(), test.getExpected().getName());
                                } else if (test.getResultPublishName() == null || test.getResultPublishName().equals("result")) {
                                    String p = PublishRules.buildTestPublishResultName(test);
                                    test.addPublish(Utils.unCapitalizeFirstLetter(p), test.getExpected().getName());
                                } else {
                                    test.addPublish(test.getResultPublishName(), test.getExpected().getName());
                                }
                            }
                        }
                        return item;
                    }


                    @Override
                    public Item visitNameRecognition(Item item) {
                        if (item instanceof Test) {
                            Test test = (Test) item;
                            PluginProvider pluginProvider = ProviderManager.provider("Naming");
                            if (pluginProvider != null) {
                                Provider provider = pluginProvider.create();
                                item = provider.Analyze(tree, item, null);
                            }
                        }
                        return item;
                    }

                     */
                });
        }
        new TreeImpl(suite).findProtocols();
    }
}
