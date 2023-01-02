package com.aallam.openai.sample.jvm

import com.aallam.openai.api.ExperimentalOpenAI
import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.image.ImageCreationURL
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.api.moderation.ModerationRequest
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

var isVerbose = false

fun printOut(outString: Any) {
    if (isVerbose) {
        if (outString.toString().subSequence(0, 2).contains('>')) {
            println(outString)
            return
        }
        println("> $outString")
    }
}

@OptIn(ExperimentalOpenAI::class)
fun main() = runBlocking {
    val apiKey = System.getenv("OPENAI_API_KEY")
    val token = requireNotNull(apiKey) { "OPENAI_API_KEY environment variable must be set." }
    val openAI = OpenAI(token)

    printOut("> Getting available engines...")
    openAI.models().forEach(::println)

    printOut("\n> Getting ada engine...")

    val adaModel = "text-ada-001"
    val codeDaVinci = "code-davinci-002"
    val daVinciBeast = "text-davinci-003"

    val activeModel = openAI.model(modelId = ModelId(codeDaVinci))
    printOut(activeModel)

    printOut("\n>️ Creating completion...")
    val completionRequest = CompletionRequest(
        model = activeModel.id,
        prompt = "Somebody once told me the world is gonna roll me"
    )
    openAI.completion(completionRequest).choices.forEach(::println)

    printOut("\n>️ Creating completion stream...")
    openAI.completions(completionRequest)
        .onEach { print(it.choices[0].text) }
        .onCompletion { println() }
        .launchIn(this)
        .join()

    printOut("\n> Read files...")
    val files = openAI.files()
    printOut(files)

    printOut("\n> Create moderations...")
    val moderation = openAI.moderations(
        request = ModerationRequest(
            input = "I want to kill them."
        )
    )
    printOut(moderation)

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
