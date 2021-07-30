package com.ljw.okttestapp.util

import org.openkoreantext.processor.OpenKoreanTextProcessorJava
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor
import org.openkoreantext.processor.tokenizer.KoreanTokenizer
import scala.collection.Seq

object JavaOpenKoreanTextProcessorExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val text = "한국어를 처리하는 예시입니닼ㅋㅋㅋㅋㅋ #한국어"

        // Normalize
        val normalized: CharSequence = OpenKoreanTextProcessorJava.normalize(text)
        println(normalized)
        // 한국어를 처리하는 예시입니다ㅋㅋ #한국어

        // Tokenize
        val tokens =
            OpenKoreanTextProcessorJava.tokenize(normalized) as Seq<KoreanTokenizer.KoreanToken>
        println(OpenKoreanTextProcessorJava.tokensToJavaStringList(tokens))
        // [한국어, 를, 처리, 하는, 예시, 입니, 다, ㅋㅋ, #한국어]
        println(OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens))
        // [한국어(Noun: 0, 3), 를(Josa: 3, 1), 처리(Noun: 5, 2), 하는(Verb(하다): 7, 2), 예시(Noun: 10, 2),
        // 입니다(Adjective(이다): 12, 3), ㅋㅋㅋ(KoreanParticle: 15, 3), #한국어(Hashtag: 19, 4)]

        // Phrase extraction
        val phrases: List<KoreanPhraseExtractor.KoreanPhrase> =
            OpenKoreanTextProcessorJava.extractPhrases(tokens, true, true)
        println(phrases)
        // [한국어(Noun: 0, 3), 처리(Noun: 5, 2), 처리하는 예시(Noun: 5, 7), 예시(Noun: 10, 2), #한국어(Hashtag: 18, 4)]
    }
}