package com.bookstore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum Language {
    NEPALI("Nepali"),
    MACEDONIAN("Macedonian"),
    ESTONIAN("Estonian"),
    BENGALI("Bengali"),
    GEORGIAN("Georgian"),
    TAMIL("Tamil"),
    AUSTRALIAN_LANGUAGES("Australian languages"),
    SLOVAK("Slovak"),
    DUTCH("Dutch"),
    SPANISH("Spanish"),
    ARABIC("Arabic"),
    KURDISH("Kurdish"),
    FRENCH("French"),
    FARSI("Farsi"),
    PANJABI("Panjabi; Punjabi"),
    FAROESE("Faroese"),
    UKRAINIAN("Ukrainian"),
    THAI("Thai"),
    KANNADA("Kannada"),
    CZECH("Czech"),
    AMHARIC("Amharic"),
    KOREAN("Korean"),
    RUSSIAN("Russian"),
    POLISH("Polish"),
    ALEUT("Aleut"),
    LATVIAN("Latvian"),
    MALAY("Malay"),
    PERSIAN("Persian"),
    FILIPINO("Filipino; Pilipino"),
    CROATIAN("Croatian"),
    CHINESE("Chinese"),
    NORWEGIAN("Norwegian"),
    INDONESIAN("Indonesian"),
    MALAYALAM("Malayalam"),
    NORWEGIAN_BOKMAL("Bokmål, Norwegian; Norwegian Bokmål"),
    ENGLISH("English"),
    MULTIPLE_LANGUAGES("Multiple languages"),
    DUALA("Duala"),
    DANISH("Danish"),
    BULGARIAN("Bulgarian"),
    CATALAN("Catalan; Valencian"),
    TURKISH("Turkish"),
    URDU("Urdu"),
    TAGALOG("Tagalog"),
    BOSNIAN("Bosnian"),
    ALBANIAN("Albanian"),
    JAPANESE("Japanese"),
    SLOVENIAN("Slovenian"),
    MODERN_GREEK("Greek, Modern (1453-)"),
    MALTESE("Maltese"),
    GALICIAN("Galician"),
    GUJARATI("Gujarati"),
    ARMENIAN("Armenian"),
    MAYAN_LANGUAGES("Mayan languages"),
    UNKNOWN(""), // Handles empty string
    HUNGARIAN("Hungarian"),
    BASQUE("Basque"),
    ICELANDIC("Icelandic"),
    MIDDLE_DUTCH("Dutch, Middle (ca.1050-1350)"),
    AFRIKAANS("Afrikaans"),
    MARATHI("Marathi"),
    GERMAN("German"),
    ITALIAN("Italian"),
    NORWEGIAN_NYNORSK("Norwegian Nynorsk; Nynorsk, Norwegian"),
    SWEDISH("Swedish"),
    VIETNAMESE("Vietnamese"),
    FINNISH("Finnish"),
    PORTUGUESE("Portuguese"),
    HINDI("Hindi"),
    LITHUANIAN("Lithuanian"),
    MIDDLE_ENGLISH("English, Middle (1100-1500)"),
    ROMANIAN("Romanian"),
    ANCIENT_GREEK("Greek, Ancient (to 1453)"),
    MIDDLE_FRENCH("French, Middle (ca.1400-1600)");
    private final  String displayName;

    public static Language fromLanguageName(String input) {

        if (input == null || input.isBlank()) {
            return Language.UNKNOWN;
        }
        return Arrays.stream(Language.values())
                .filter(lang ->lang.displayName.equalsIgnoreCase(input))
                .findFirst()
                .orElse(Language.UNKNOWN);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
