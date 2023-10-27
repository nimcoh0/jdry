package org.softauto.analyzer.core.system.nlp;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import java.io.IOException;
import java.io.InputStream;

public class NlpHelper {


    public static void main(String[] args) throws IOException {
        new NlpHelper().run1();
    }


    public void run1(){
        try{
            // test sentence
            String[] tokens = new String[]{"get", "Company", "User"};

            // Parts-Of-Speech Tagging
            // reading parts-of-speech model to a stream
            //InputStream posModelIn = new FileInputStream("models"+File.separator+"en-pos-maxent.bin");
            InputStream posModelIn = getClass().getClassLoader().getResourceAsStream("org/softauto/analyzer/core/system/nlp/lib/en-pos-maxent.bin");
            // loading the parts-of-speech model from stream
            POSModel posModel = new POSModel(posModelIn);
            // initializing the parts-of-speech tagger with model
            POSTaggerME posTagger = new POSTaggerME(posModel);
            // Tagger tagging the tokens
            String tags[] = posTagger.tag(tokens);

            // reading the chunker model
            //InputStream ins = new FileInputStream("models"+File.separator+"en-chunker.bin");
            InputStream ins = getClass().getClassLoader().getResourceAsStream("org/softauto/analyzer/core/system/nlp/lib/en-chunker.bin");
            // loading the chunker model
            ChunkerModel chunkerModel = new ChunkerModel(ins);
            // initializing chunker(maximum entropy) with chunker model
            ChunkerME chunker = new ChunkerME(chunkerModel);
            // chunking the given sentence : chunking requires sentence to be tokenized and pos tagged
            String[] chunks = chunker.chunk(tokens,tags);

            // printing the results
            System.out.println("\nChunker Example in Apache OpenNLP\nPrinting chunks for the given sentence...");
            System.out.println("\nTOKEN - POS_TAG - CHUNK_ID\n-------------------------");
            for(int i=0;i< chunks.length;i++){
                System.out.println(tokens[i]+" - "+tags[i]+" - "+chunks[i]);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    public void run() throws IOException {
        //SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        //String[] tokens = tokenizer
               // .tokenize("getCompanyUser");
        InputStream in = getClass().getClassLoader().getResourceAsStream("en-chunker.bin");
        ChunkerModel model = new ChunkerModel(in);
        Parse topParses[] = ParserTool.parseLine(line, parser, 1);

        TokenizerME tokenizer = new TokenizerME(model);
        String tokens[] = tokenizer.tokenize("get Company User");
        double tokenProbs[] = tokenizer.getTokenProbabilities();


        //TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);
        //NameFinderME nameFinderME = new NameFinderME(model);
       // List<Span> spans = Arrays.asList(nameFinderME.find(tokens));
       // model.getSequenceCodec();
        //assertThat(spans.toString())
             //   .isEqualTo("[[0..1) person, [13..14) person, [20..21) person]");
    }

     */

}
