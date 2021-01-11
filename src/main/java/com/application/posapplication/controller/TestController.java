package com.application.posapplication.controller;

import com.application.posapplication.model.Type;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.application.posapplication.model.Type.*;

@RestController
@RequestMapping(value = "/api/v1")
public class TestController {

    @Autowired
    private StanfordCoreNLP stanfordCoreNLP;

    private List<String> collectList(List<CoreLabel> coreLabels, final Type type){
        return coreLabels
                .stream()
                .filter(coreLabel -> type.getName().equalsIgnoreCase(coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class)))
                .map(CoreLabel::originalText)
                .collect(Collectors.toList());
    }

    @PostMapping
    @RequestMapping(value="/ssplit")
//    public List<CoreSentence> ssplit(String input){
    public ResponseEntity<String> ssplit(@RequestBody String input, Type type) {
        //String inputTest = "This is the first sentence. This is the second sentence. This is the third sentence.";
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> coreSentences = coreDocument.sentences();


        for (CoreSentence sentence : coreSentences) {
            CoreDocument coreDocument1 = new CoreDocument(String.valueOf(sentence));
            stanfordCoreNLP.annotate(coreDocument1);
        }

        //Autocompleted by java
        //return coreSentences;
        //Baca dokumentasi ResponseEntity
        return ResponseEntity.ok().body(coreSentences.toString());
        //return new HashSet<>(collectList(coreSentences, type));
    }

    @PostMapping
    @RequestMapping(value="/pos")
    public Set<String> pos(@RequestBody final String input, @RequestParam final Type type){

        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabels = coreDocument.tokens();

        return new HashSet<>();
    }

    @PostMapping
    @RequestMapping(value="/posnoun")
    public Set<String> posnoun(@RequestBody final String input) {
//    //public Set<String> pos(@RequestBody final String input, Type type){
        Type[] listoftype = new Type[]{
                NNPS,
                NNS,
                NNP,
                NN
        };

        HashSet<String> tmp = new HashSet();
        for (int i = 0; i < listoftype.length; i++) {
            CoreDocument coreDocument = new CoreDocument(input);
            stanfordCoreNLP.annotate(coreDocument);
            List<CoreLabel> coreLabels = coreDocument.tokens();
            HashSet<String> doub = new HashSet<>(collectList(coreLabels, listoftype[i]));
            tmp.addAll(doub);
        }
        return tmp;
    }

    @PostMapping
    @RequestMapping(value="/posverb")
    public Set<String> posverb(@RequestBody final String input){

        Type[] listoftype = new Type[]{
          VB,
          VBD,
          VBG,
          VBN,
          VBP,
          VBZ
        };

        HashSet<String> tmp = new HashSet();
        for(int i = 0; i < listoftype.length; i++){
            CoreDocument coreDocument = new CoreDocument(input);
            stanfordCoreNLP.annotate(coreDocument);
            List<CoreLabel> coreLabels = coreDocument.tokens();
            HashSet<String> doub = new HashSet<>(collectList(coreLabels, listoftype[i]));
            tmp.addAll(doub);
        }
        return tmp;
        //return null;
    }
}
