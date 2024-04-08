package nesrinasan.SpringAI;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;


    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ask")
    public String generate(@RequestParam String message) {
        return chatClient.call(message);

    }

    @GetMapping("/askWithPrompt")
    public ChatResponse generatePrompt(@RequestBody AskRequestDto askRequestDto) {
        String ask = askRequestDto.ask();
        UserMessage userMessage = new UserMessage(ask);
        Prompt prompt = new Prompt(userMessage);

        return chatClient.call(prompt);

    }

    /**
     * MEssageType: exists to indicate who the artificial intelligence is communicating with.
     * There may be a difference in terms of dialogue between a conversation with AI where the end user is a USER and a conversation with a bot.
     * For example, a question from the user may be processed differently, while a conversation initiated by the AI assistant itself may be evaluated in a different context.
     * This is done as part of the dialogue system design to ensure the flow of dialogue is more natural and human-like.
     *
     * @param askRequestDto
     * @return
     */
    @GetMapping("/askAsistantWithPrompt")
    public ChatResponse askAsistantWithPrompt(@RequestBody AskRequestDto askRequestDto) {
        String ask = askRequestDto.ask();
        AssistantMessage userMessage = new AssistantMessage(ask);
        Prompt prompt = new Prompt(userMessage);

        return chatClient.call(prompt);

    }

    @GetMapping("/askWithOptions")
    public ChatResponse askWithOptions(@RequestBody AskRequestDto askRequestDto) {
        String ask = askRequestDto.ask();
        UserMessage userMessage = new UserMessage(ask);

        return chatClient.call(
                new Prompt(
                        userMessage.getContent(),
                        OpenAiChatOptions.builder()
                                .withModel("gpt-4")
                                .withTemperature(0.4f) //randomness
                                .withMaxTokens(80)//It limits the text the model produces..
                                .withPresencePenalty(0.6f) //  Increases or decreases the likelihood that the model will use previously used words.
                                .withFrequencyPenalty(0.0f) //used to reduce or increase the frequency of use of certain words in the responses produced by the model
                                .build()
                ));
    }

}
