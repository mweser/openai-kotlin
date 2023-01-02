package com.aallam.openai.sample.jvm

import com.aallam.openai.api.ExperimentalOpenAI
import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.runBlocking

var isVerbose = false
var horizLine = "\n#************************\n"
var useCommentFormat = true

fun sandwich(string: String): String {
    return "\"\"\"\n$string\n\"\"\"\n"

}

fun printOut(outString: Any, overrideVerbose: Boolean = false) {
    if (isVerbose || overrideVerbose) {
        if (outString.toString().subSequence(0, 2).contains('>')) {
            println(outString)
            return
        }
        println("$outString")
    }
}

fun printWrap(string: String) {
    print("$horizLine$string$horizLine")
}

@OptIn(ExperimentalOpenAI::class)
fun main() = runBlocking {
    val apiKey = System.getenv("OPENAI_API_KEY")
    val token = requireNotNull(apiKey) { "OPENAI_API_KEY environment variable must be set." }
    val openAI = OpenAI(token)

    printOut("> Getting available engines...")
//    openAI.models().forEach(::println)

    printOut("\n> Getting ada engine...")

    val codeDaVinci = "code-davinci-002"
    val codeCushman = "code-cushman-001"

    val adaModel = "text-ada-001"
    val daVinciBeast = "text-davinci-003"

    val activeModel = openAI.model(modelId = ModelId(codeCushman))
    printOut(activeModel)

    val promptBody = """
        |# Python 3 
        |Write a script that fetches the latest ETH price and display it 
    """.trimMargin()


    val promptOut = if (useCommentFormat) {
        sandwich(promptBody)
    } else {
        promptBody
    }

    printOut("\n>️ Creating completion...", overrideVerbose = true)
    val completionRequest = CompletionRequest(
        model = activeModel.id,
        prompt = promptOut,
        maxTokens = 1900,
        temperature = 0.0,
    )

    val completionOut = openAI.completion(completionRequest)
    val resultText = completionOut.choices[0].text.trim()

    printWrap("# PROMPT:\n$promptOut")
    printOut(resultText, overrideVerbose = true)

//    printOut("\n>️ Creating completion stream...", overrideVerbose = true)
//    openAI.completions(completionRequest)
//        .onEach { print(it.choices[0].text) }
//        .onCompletion { println() }
//        .launchIn(this)
//        .join()

//    printOut("\n> Read files...")
//    val files = openAI.files()
//    printOut(files)

//    printOut("\n> Create moderations...")
//    val moderation = openAI.moderations(
//        request = ModerationRequest(
//            input = "I want to kill them."
//        )
//    )
//    printOut(moderation)

//    printOut("\n> Create images...")
//    val images = openAI.image(
//        creation = ImageCreationURL(
//            prompt = "A cute baby sea otter",
//            n = 2,
//            size = ImageSize.is1024x1024
//        )
//    )
//    printOut(images)
}
