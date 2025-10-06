package com.proyectofinal.proyectofinal.utils;

import com.proyectofinal.proyectofinal.AbstractTest;
import com.proyectofinal.proyectofinal.dto.IAResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IAResponseParserTest extends AbstractTest {
        @Test
        void parse_nullOrBlankJson_returnsEmptyResponse() {
            IAResponseDTO result1 = IAResponseParser.parse(null);
            IAResponseDTO result2 = IAResponseParser.parse("");
            IAResponseDTO result3 = IAResponseParser.parse("   ");

            assertEquals("", result1.getUserResponse());
            assertEquals("", result1.getTicketContent());

            assertEquals("", result2.getUserResponse());
            assertEquals("", result2.getTicketContent());

            assertEquals("", result3.getUserResponse());
            assertEquals("", result3.getTicketContent());
        }

        @Test
        void parse_malformedJson_returnsEmptyResponse() {
            String malformed = "{user: 'hola'"; // falta cierre y comillas correctas
            IAResponseDTO result = IAResponseParser.parse(malformed);

            assertEquals("", result.getUserResponse());
            assertEquals("", result.getTicketContent());
        }

        @Test
        void parse_validJsonWithBothKeys_returnsCorrectValues() {
            String json = """
                {
                    "USER_RESPONSE": "Hola usuario",
                    "TICKET_CONTENT": "Contenido del ticket"
                }
                """;

            IAResponseDTO result = IAResponseParser.parse(json);

            assertEquals("Hola usuario", result.getUserResponse());
            assertEquals("Contenido del ticket", result.getTicketContent());
        }

        @Test
        void parse_jsonMissingUserResponse_returnsEmptyUserResponse() {
            String json = """
                {
                    "TICKET_CONTENT": "Contenido del ticket"
                }
                """;

            IAResponseDTO result = IAResponseParser.parse(json);

            assertEquals("", result.getUserResponse());
            assertEquals("Contenido del ticket", result.getTicketContent());
        }

        @Test
        void parse_jsonMissingTicketContent_returnsEmptyTicketContent() {
            String json = """
                {
                    "USER_RESPONSE": "Hola usuario"
                }
                """;

            IAResponseDTO result = IAResponseParser.parse(json);

            assertEquals("Hola usuario", result.getUserResponse());
            assertEquals("", result.getTicketContent());
        }

        @Test
        void parse_jsonWithNullValues_returnsEmptyStrings() {
            String json = """
                {
                    "USER_RESPONSE": null,
                    "TICKET_CONTENT": null
                }
                """;

            IAResponseDTO result = IAResponseParser.parse(json);

            assertEquals("", result.getUserResponse());
            assertEquals("", result.getTicketContent());
        }

        @Test
        void parse_jsonWithEmptyStrings_returnsEmptyStrings() {
            String json = """
                {
                    "USER_RESPONSE": "",
                    "TICKET_CONTENT": ""
                }
                """;

            IAResponseDTO result = IAResponseParser.parse(json);

            assertEquals("", result.getUserResponse());
            assertEquals("", result.getTicketContent());
        }

        @Test
        void parse_jsonWithExtraKeys_ignoresThem() {
            String json = """
                {
                    "USER_RESPONSE": "Hola usuario",
                    "TICKET_CONTENT": "Contenido ticket",
                    "EXTRA_KEY": "Algo irrelevante"
                }
                """;

            IAResponseDTO result = IAResponseParser.parse(json);

            assertEquals("Hola usuario", result.getUserResponse());
            assertEquals("Contenido ticket", result.getTicketContent());
        }
}
