package model.dto

import java.time.Instant

case class ParsingDTO(createTimestamp: Instant,
                      timestamp: Instant,
                      title: String,
                      language: String,
                      wiki: String,
                      categories: Seq[String],
                      auxilaryTexts: Seq[String])
