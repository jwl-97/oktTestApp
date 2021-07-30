package com.ljw.okttestapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.openkoreantext.processor.OpenKoreanTextProcessorJava

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val text = "방울 방울 보라색 물 풍선 한가득"
        val text = "워라밸을 지키기엔 일정이 너무 바빠"

        getOktTokenizerText(text.removeSpecialCharacter())
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    private fun getOktTokenizerText(text: String) {
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                val oktTextFromAPI = getOktTokenizerTextFromAPI(text)
                val oktTextFromJAR = getOktTokenizerTextFromJAR(text)

                Log.d("OKTTEST",
                        "text : $text\n" +
                                "oktTextFromAPI : $oktTextFromAPI\n" +
                                "oktTextFromJAR: $oktTextFromJAR")
            }.join()
        }
    }

    /** API 사용 어구추출 (단어사전 추가 불가) */
    private suspend fun getOktTokenizerTextFromAPI(text: String): List<String>? {
        val response =
                RetrofitClientV2.iMyService.getOktTokenizerText("https://open-korean-text-api.herokuapp.com/extractPhrases?text=${text}")

        val result = response.body()?.string().toString()
        val phrases = JSONObject(result).optJSONArray("phrases") ?: return null

        return parsingText(phrases)
    }

    /** JAR 사용 어구추출 (단어사전 추가) */
    private fun getOktTokenizerTextFromJAR(text: String): List<String>? {
        //com.ljw.oktTestApp.OktTextExample 참고
        val tokens = OpenKoreanTextProcessorJava.tokenize(text)
        val phrases = OpenKoreanTextProcessorJava.extractPhrases(tokens, true, true)

        return parsingText(phrases)
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    private fun parsingText(list: Any): List<String>? {
        var resultList = mutableListOf<String>()

        when (list) {
            is JSONArray -> {
                resultList = mutableListOf<String>()
                for (i in 0 until list.length()) {
                    val list = list[i].toString().parsingText()
                    if (list != "") resultList.add(list)
                }
            }

            is MutableList<*> -> {
                resultList = mutableListOf<String>()

                list.forEach {
                    val text = it.toString().parsingText()

                    if (text != "") {
                        resultList.add(text)
                    }
                }
            }

            else -> return null
        }

        Log.d("OKTTEST", "parsingText : ${resultList.joinToString(", ")}")
        return resultList.distinct()
    }

    private fun String.parsingText(): String {
        return this.replace(" ", "") //빈칸 제거
                .replace("\\((.*?)\\)".toRegex(), "")
                .replace("[^(ㄱ-ㅎ|ㅏ-ㅣ|가-힣))]".toRegex(), "") //한글이 아닌 문자 제거
    }

    /** 특수문자 제거 */
    private fun String.removeSpecialCharacter(): String {
        return this.trim().replace("[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]".toRegex(), "")
                .replace("\"", " ")
    }
}