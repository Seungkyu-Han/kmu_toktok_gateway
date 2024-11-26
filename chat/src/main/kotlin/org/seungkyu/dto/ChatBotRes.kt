package org.seungkyu.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class ChatGPTRes(
    val id: String,
    @JsonProperty("object")
    val objectType: String,
    @JsonProperty("created_at")
    val createdAt: Long,
    @JsonProperty("assistant_id")
    val assistantId: String?,
    @JsonProperty("thread_id")
    val threadId: String?,
    @JsonProperty("run_id")
    val runId: String?,
    val status: String?,
    @JsonProperty("incomplete_details")
    val incompleteDetails: String?,
    @JsonProperty("incomplete_at")
    val incompleteAt: Long?,
    @JsonProperty("completed_at")
    val completedAt: Long?,
    val role: String?,
    val content: List<Content>?,
    val attachments: List<Any>?,
    val metadata: Map<String, Any>?,
    val delta: Delta?
)

data class Delta(
    val content: List<Content>?,
)

data class Content(
    val index: Int?,
    val type: String,
    val text: Text
)

data class Text(
    val value: String,
    val annotations: List<Annotation>?
)

data class Annotation(
    val type: String,
    val text: String,
    @JsonProperty("start_index")
    val startIndex: Int,
    @JsonProperty("end_index")
    val endIndex: Int,
    @JsonProperty("file_citation")
    val fileCitation: FileCitation?
)

data class FileCitation(
    @JsonProperty("file_id")
    val fileId: String?
)

data class ThreadCreatedEvent(
    val id: String?,
    val `object`: String?,
    val created_at: Instant?,
    val metadata: Map<String, Any?>?
)

data class ThreadRunCreatedEvent(
    val id: String,
    val `object`: String,
    val created_at: Instant,
    val assistant_id: String,
    val thread_id: String,
    val status: String,
    val started_at: Instant?,
    val expires_at: Instant,
    val cancelled_at: Instant?,
    val failed_at: Instant?,
    val completed_at: Instant?,
    val required_action: Any?,
    val last_error: String?,
    val model: String,
    val instructions: String?,
    val tools: List<Tool>,
    val metadata: Map<String, Any?>,
    val temperature: Double,
    val top_p: Double,
    val max_completion_tokens: Int?,
    val max_prompt_tokens: Int?,
    val truncation_strategy: TruncationStrategy,
    val incomplete_details: Any?,
    val usage: Usage?,
    val response_format: String,
    val tool_choice: String,
    val parallel_tool_calls: Boolean
)

data class Tool(
    val type: String,
    val function: ToolFunction
)

data class ToolFunction(
    val name: String,
    val description: String,
    val parameters: FunctionParameters
)

data class FunctionParameters(
    val type: String,
    val properties: Map<String, Property>
)

data class Property(
    val type: String,
    val description: String,
    val enum: List<String>? = null
)

data class TruncationStrategy(
    val type: String,
    val last_messages: Any?
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class StepCreatedEvent(
    val id: String,
    val `object`: String,
    val created_at: Instant,
    val run_id: String,
    val assistant_id: String,
    val thread_id: String,
    val type: String,
    val status: String,
    val cancelled_at: Instant?,
    val completed_at: Instant?,
    val expires_at: Instant,
    val failed_at: Instant?,
    val last_error: String?,
    val step_details: StepDetails,
    val usage: Usage?
)

data class StepDetails(
    val type: String,
    val tool_calls: List<ToolCall>
)

data class ToolCall(
    val index: Int,
    val id: String?,
    val type: String,
    val function: ToolFunctionDetail
)

data class ToolFunctionDetail(
    val name: String,
    val arguments: String?,
    val output: Any?
)

data class RequiredAction(
    val type: String,
    val submit_tool_outputs: SubmitToolOutputs
)

data class SubmitToolOutputs(
    val tool_calls: List<ToolOutputCall>
)

data class ToolOutputCall(
    val id: String,
    val type: String,
    val function: ToolFunctionArgument
)

data class ToolFunctionArgument(
    val name: String,
    val arguments: String
)
