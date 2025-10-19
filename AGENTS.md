# Developer Guide

- In DTOs, use `class` instead of `data class`.
- For logging, use `private val log = KotlinLogging.logger {}` at the top level (outside the class).
- Use the suffix `At` for all fields of type LocalDateTime.
