package com.ljw.okttestapp

import com.twitter.penguin.korean.TwitterKoreanProcessorJava

object OktTextExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val text = "한국어를 처리하는 예시입니닼ㅋㅋㅋㅋㅋ #한국어"

        // Normalize
        val normalized = TwitterKoreanProcessorJava.normalize(text)
        println(normalized)
        // 한국어를 처리하는 예시입니다ㅋㅋ #한국어


        // Tokenize
        val tokens = TwitterKoreanProcessorJava.tokenize(normalized)
        println(TwitterKoreanProcessorJava.tokensToJavaStringList(tokens))
        // [한국어, 를, 처리, 하는, 예시, 입니, 다, ㅋㅋ, #한국어]
        println(TwitterKoreanProcessorJava.tokensToJavaKoreanTokenList(tokens))
        // [한국어(Noun: 0, 3), 를(Josa: 3, 1),  (Space: 4, 1), 처리(Noun: 5, 2), 하는(Verb: 7, 2),  (Space: 9, 1), 예시(Noun: 10, 2), 입니(Adjective: 12, 2), 다(Eomi: 14, 1), ㅋㅋ(KoreanParticle: 15, 2),  (Space: 17, 1), #한국어(Hashtag: 18, 4)]


        // Stemming
        val stemmed = TwitterKoreanProcessorJava.stem(tokens)
        println(TwitterKoreanProcessorJava.tokensToJavaStringList(stemmed))
        // [한국어, 를, 처리, 하다, 예시, 이다, ㅋㅋ, #한국어]
        println(TwitterKoreanProcessorJava.tokensToJavaKoreanTokenList(stemmed))
        // [한국어(Noun: 0, 3), 를(Josa: 3, 1),  (Space: 4, 1), 처리(Noun: 5, 2), 하다(Verb: 7, 2),  (Space: 9, 1), 예시(Noun: 10, 2), 이다(Adjective: 12, 3), ㅋㅋ(KoreanParticle: 15, 2),  (Space: 17, 1), #한국어(Hashtag: 18, 4)]


        // Phrase extraction
        val phrases = TwitterKoreanProcessorJava.extractPhrases(tokens, true, true)
        println(phrases)
        // [한국어(Noun: 0, 3), 처리(Noun: 5, 2), 처리하는 예시(Noun: 5, 7), 예시(Noun: 10, 2), #한국어(Hashtag: 18, 4)]
    }
}