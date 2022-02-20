package utils

import play.api.libs.json.JsValue

import java.time.Instant

object JsonHelper {
    
    def findInstant(input: JsValue, field: String): Option[Instant] = {
        (input \ field).asOpt[Instant]
    }
    
    def findString(input: JsValue, field: String): Option[String] = {
        (input \ field).asOpt[String]
    }
    
    def findStrings(input: JsValue, field: String): Option[Seq[String]] = {
        (input \ field).asOpt[Seq[String]]
    }
}
