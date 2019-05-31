package newton.travelassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlagAPI {

    protected String getFlagURL(String currencyCode) {
        if (new FlagAPI().getInfo(currencyCode) != null) {
            String countryCode = new FlagAPI().getInfo(currencyCode)[1].toLowerCase();
            return "https://www.countryflags.io/" + countryCode + "/flat/64.png";
        } else {
            return "";
        }
    }

    protected String getCurrencyFullName(String currencyCode) {
        if (new FlagAPI().getInfo(currencyCode) != null) {
            return new FlagAPI().getInfo(currencyCode)[0];
        } else {
            return "";
        }
    }

    protected String[] getInfo(String currencyCode) {
        Map<String, String[]> infoMap = init();

        if (infoMap.get(currencyCode) != null) {
            return infoMap.get(currencyCode);
        } else {
            return null;
        }
    }

    protected List<String> getAllCurrencyCode() {
        Map<String, String[]> infoMap = init();
        List<String> allKeys = new ArrayList<>(infoMap.keySet());
        return allKeys;
    }

    protected Map<String, String[]> init() {
        Map<String, String[]> infoMap = new HashMap<>();
//        infoMap.put("currencyCode", new String[]{"currencyName", "countryCode", "countryName"});
        infoMap.put("AED", new String[]{"Dirham", "AE", "United Arab Emirates"});
        infoMap.put("AFN", new String[]{"Afghani", "AF", "Afghanistan"});
        infoMap.put("ALL", new String[]{"Albanian Lek", "AL", "Albania"});
        infoMap.put("AMD", new String[]{"Armenian Dram", "AM", "Armenia"});
        infoMap.put("ANG", new String[]{"Netherlands Antilles Guilder", "AN", "Netherlands Antilles"});
        infoMap.put("AOA", new String[]{"Angolan kwanza", "AO", "Angola"});
        infoMap.put("AQD", new String[]{"Antarctican dollar", "AQ", "Antarctica"});
        infoMap.put("ARS", new String[]{"Peso", "AR", "Argentina"});
        infoMap.put("AUD", new String[]{"Australian Dollars", "AU", "Australian"});
        infoMap.put("AZN", new String[]{"Manat", "AZ", "Azerbaijan"});
        infoMap.put("BAM", new String[]{"Bosnia and Herzegovina convertible mark", "BA", "Bosnia and Herzegovina"});
        infoMap.put("BBD", new String[]{"Barbadian Dollar", "BB", "Barbados"});
        infoMap.put("BDT", new String[]{"Taka", "BD", "Bangladesh"});
        infoMap.put("BGN", new String[]{"Lev", "BG", "Bulgaria"});
        infoMap.put("BHD", new String[]{"Bahraini Dinar", "BH", "Bahrain"});
        infoMap.put("BIF", new String[]{"Burundi Franc", "BI", "Burundi"});
        infoMap.put("BMD", new String[]{"Bermudian Dollar", "BM", "Bermuda"});
        infoMap.put("BND", new String[]{"Bruneian Dollar", "BN", "Brunei Darussalam"});
        infoMap.put("BOB", new String[]{"Boliviano", "BO", "Bolivia"});
        infoMap.put("BRL", new String[]{"Brazil", "BR", "Brazil"});
        infoMap.put("BSD", new String[]{"Bahamian Dollar", "BS", "Bahamas"});
        infoMap.put("BWP", new String[]{"Pula", "BW", "Botswana"});
        infoMap.put("BYR", new String[]{"Belarus Ruble", "BY", "Belarus"});
        infoMap.put("BZD", new String[]{"Belizean Dollar", "BZ", "Belize"});
        infoMap.put("CAD", new String[]{"Canadian Dollar", "CA", "Canada"});
        infoMap.put("CDF", new String[]{"Congolese Frank", "CD", "Congo (Kinshasa)"});
        infoMap.put("CHF", new String[]{"Swiss Franc", "CH", "Switzerland"});
        infoMap.put("CLP", new String[]{"Chilean Peso", "CL", "Chile"});
        infoMap.put("CNY", new String[]{"Yuan Renminbi", "CN", "China"});
        infoMap.put("COP", new String[]{"Peso", "CO", "Colombia"});
        infoMap.put("CRC", new String[]{"Costa Rican Colon", "CR", "Costa Rica"});
        infoMap.put("CUP", new String[]{"Cuban Peso", "CU", "Cuba"});
        infoMap.put("CVE", new String[]{"Escudo", "CV", "Cape Verde"});
        infoMap.put("CYP", new String[]{"Cypriot Pound", "CY", "Cyprus"});
        infoMap.put("CZK", new String[]{"Koruna", "CZ", "Czech Republic"});
        infoMap.put("DJF", new String[]{"Djiboutian Franc", "DJ", "Djibouti"});
        infoMap.put("DKK", new String[]{"Danish Krone", "DK", "Denmark"});
        infoMap.put("DOP", new String[]{"Dominican Peso", "DO", "Dominican Republic"});
        infoMap.put("DZD", new String[]{"Algerian Dinar", "DZ", "Algeria"});
        infoMap.put("ECS", new String[]{"Sucre", "EC", "Ecuador"});
        infoMap.put("EEK", new String[]{"Estonian Kroon", "EE", "Estonia"});
        infoMap.put("EGP", new String[]{"Egyptian Pound", "EG", "Egypt"});
        infoMap.put("ETB", new String[]{"Ethiopian Birr", "ET", "Ethiopia"});
        infoMap.put("EUR", new String[]{"Euros", "EU", "Euro"});
        infoMap.put("FJD", new String[]{"Fijian Dollar", "FJ", "Fiji"});
        infoMap.put("FKP", new String[]{"Falkland Pound", "FK", "Falkland Islands (Malvinas)"});
        infoMap.put("GBP", new String[]{"Great Britain Pounds", "GB", "United Kingdom"});
        infoMap.put("GEL", new String[]{"Lari", "GE", "Georgia"});
        infoMap.put("GGP", new String[]{"Guernsey pound", "GG", "Guernsey"});
        infoMap.put("GHS", new String[]{"Ghana cedi", "GH", "Ghana"});
        infoMap.put("GIP", new String[]{"Gibraltar Pound", "GI", "Gibraltar"});
        infoMap.put("GMD", new String[]{"Dalasi", "GM", "Gambia"});
        infoMap.put("GNF", new String[]{"Guinean Franc", "GN", "Guinea"});
        infoMap.put("GTQ", new String[]{"Quetzal", "GT", "Guatemala"});
        infoMap.put("GYD", new String[]{"Guyanaese Dollar", "GY", "Guyana"});
        infoMap.put("HKD", new String[]{"Hong Kong Dollar", "HK", "Hong Kong"});
        infoMap.put("HNL", new String[]{"Lempira", "HN", "Honduras"});
        infoMap.put("HRK", new String[]{"Croatian Dinar", "HR", "Croatia (Hrvatska)"});
        infoMap.put("HTG", new String[]{"Gourde", "HT", "Haiti"});
        infoMap.put("HUF", new String[]{"Forint", "HU", "Hungary"});
        infoMap.put("IDR", new String[]{"Indonesian Rupiah", "ID", "Indonesia"});
        infoMap.put("ILS", new String[]{"Shekel", "IL", "Israel"});
        infoMap.put("INR", new String[]{"Indian Rupee", "IN", "India"});
        infoMap.put("IQD", new String[]{"Iraqi Dinar", "IQ", "Iraq"});
        infoMap.put("IRR", new String[]{"Iranian Rial", "IR", "Iran (Islamic Republic of)"});
        infoMap.put("ISK", new String[]{"Icelandic Krona", "IS", "Iceland"});
        infoMap.put("JMD", new String[]{"Jamaican Dollar", "JM", "Jamaica"});
        infoMap.put("JOD", new String[]{"Jordanian Dinar", "JO", "Jordan"});
        infoMap.put("JPY", new String[]{"Japanese Yen", "JP", "Japan"});
        infoMap.put("KES", new String[]{"Kenyan Shilling", "KE", "Kenya"});
        infoMap.put("KGS", new String[]{"Som", "KG", "Kyrgyzstan"});
        infoMap.put("KHR", new String[]{"Riel", "KH", "Cambodia"});
        infoMap.put("KMF", new String[]{"Comoran Franc", "KM", "Comoros"});
        infoMap.put("KPW", new String[]{"Won", "KP", "Korea North"});
        infoMap.put("KRW", new String[]{"Won", "KR", "Korea South"});
        infoMap.put("KWD", new String[]{"Kuwaiti Dinar", "KW", "Kuwait"});
        infoMap.put("KYD", new String[]{"Caymanian Dollar", "KY", "Cayman Islands"});
        infoMap.put("KZT", new String[]{"Tenge", "KZ", "Kazakhstan"});
        infoMap.put("LAK", new String[]{"Lao kip", "LA", "Laos"});
        infoMap.put("LBP", new String[]{"Lebanese Pound", "LB", "Lebanon"});
        infoMap.put("LKR", new String[]{"Rupee", "LK", "Sri Lanka"});
        infoMap.put("LRD", new String[]{"Liberian Dollar", "LR", "Liberia"});
        infoMap.put("LSL", new String[]{"Loti", "LS", "Lesotho"});
        infoMap.put("LTL", new String[]{"Lita", "LT", "Lithuania"});
        infoMap.put("LVL", new String[]{"Lat", "LV", "Latvia"});
        infoMap.put("LYD", new String[]{"Libyan Dinar", "LY", "Libyan Arab Jamahiriya"});
        infoMap.put("MAD", new String[]{"Dirham", "MA", "Morocco"});
        infoMap.put("MDL", new String[]{"Leu", "MD", "Moldova Republic of"});
        infoMap.put("MGA", new String[]{"Malagasy Franc", "MG", "Madagascar"});
        infoMap.put("MKD", new String[]{"Denar", "MK", "Macedonia"});
        infoMap.put("MMK", new String[]{"Kyat", "MM", "Myanmar"});
        infoMap.put("MNT", new String[]{"Tugrik", "MN", "Mongolia"});
        infoMap.put("MOP", new String[]{"Pataca", "MO", "Macau"});
        infoMap.put("MRO", new String[]{"Ouguiya", "MR", "Mauritania"});
        infoMap.put("MTL", new String[]{"Maltese Lira", "MT", "Malta"});
        infoMap.put("MUR", new String[]{"Mauritian Rupee", "MU", "Mauritius"});
        infoMap.put("MVR", new String[]{"Rufiyaa", "MV", "Maldives"});
        infoMap.put("MWK", new String[]{"Malawian Kwacha", "MW", "Malawi"});
        infoMap.put("MXN", new String[]{"Peso", "MX", "Mexico"});
        infoMap.put("MYR", new String[]{"Ringgit", "MY", "Malaysia"});
        infoMap.put("MZN", new String[]{"Metical", "MZ", "Mozambique"});
        infoMap.put("NAD", new String[]{"Dollar", "NA", "Namibia"});
        infoMap.put("NGN", new String[]{"Naira", "NG", "Nigeria"});
        infoMap.put("NIO", new String[]{"Cordoba Oro", "NI", "Nicaragua"});
        infoMap.put("NOK", new String[]{"Norwegian Krone", "NO", "Norway"});
        infoMap.put("NPR", new String[]{"Nepalese Rupee", "NP", "Nepal"});
        infoMap.put("NZD", new String[]{"New Zealand Dollars", "NZ", "New Zealand"});
        infoMap.put("OMR", new String[]{"Sul Rial", "OM", "Oman"});
        infoMap.put("PAB", new String[]{"Balboa", "PA", "Panama"});
        infoMap.put("PEN", new String[]{"Nuevo Sol", "PE", "Peru"});
        infoMap.put("PGK", new String[]{"Kina", "PG", "Papua New Guinea"});
        infoMap.put("PHP", new String[]{"Peso", "PH", "Philippines"});
        infoMap.put("PKR", new String[]{"Rupee", "PK", "Pakistan"});
        infoMap.put("PLN", new String[]{"Zloty", "PL", "Poland"});
        infoMap.put("PYG", new String[]{"Guarani", "PY", "Paraguay"});
        infoMap.put("QAR", new String[]{"Rial", "QA", "Qatar"});
        infoMap.put("RON", new String[]{"Leu", "RO", "Romania"});
        infoMap.put("RSD", new String[]{"Serbian dinar", "RS", "Serbia"});
        infoMap.put("RUB", new String[]{"Ruble", "RU", "Russian Federation"});
        infoMap.put("RWF", new String[]{"Rwanda Franc", "RW", "Rwanda"});
        infoMap.put("SAR", new String[]{"Riyal", "SA", "Saudi Arabia"});
        infoMap.put("SBD", new String[]{"Solomon Islands Dollar", "SB", "Solomon Islands"});
        infoMap.put("SCR", new String[]{"Rupee", "SC", "Seychelles"});
        infoMap.put("SDG", new String[]{"Dinar", "SD", "Sudan"});
        infoMap.put("SEK", new String[]{"Swedish Krona", "SE", "Sweden"});
        infoMap.put("SGD", new String[]{"Singapore Dollar", "SG", "Singapore"});
        infoMap.put("SKK", new String[]{"Koruna", "SK", "Slovakia (Slovak Republic)"});
        infoMap.put("SLL", new String[]{"Leone", "SL", "Sierra Leone"});
        infoMap.put("SOS", new String[]{"Shilling", "SO", "Somalia"});
        infoMap.put("SRD", new String[]{"Surinamese Guilder", "SR", "Suriname"});
        infoMap.put("STD", new String[]{"Dobra", "ST", "Sao Tome and Principe"});
        infoMap.put("SVC", new String[]{"Salvadoran Colon", "SV", "El Salvador"});
        infoMap.put("SYP", new String[]{"Syrian Pound", "SY", "Syrian Arab Republic"});
        infoMap.put("SZL", new String[]{"Lilangeni", "SZ", "Swaziland"});
        infoMap.put("THB", new String[]{"Baht", "TH", "Thailand"});
        infoMap.put("TJS", new String[]{"Tajikistan Ruble", "TJ", "Tajikistan"});
        infoMap.put("TMT", new String[]{"Manat", "TM", "Turkmenistan"});
        infoMap.put("TND", new String[]{"Tunisian Dinar", "TN", "Tunisia"});
        infoMap.put("TOP", new String[]{"PaÕanga", "TO", "Tonga"});
        infoMap.put("TRY", new String[]{"Lira", "TR", "Turkey"});
        infoMap.put("TTD", new String[]{"Trinidad and Tobago Dollar", "TT", "Trinidad and Tobago"});
        infoMap.put("TWD", new String[]{"Dollar", "TW", "Taiwan"});
        infoMap.put("TZS", new String[]{"Shilling", "TZ", "Tanzania"});
        infoMap.put("UAH", new String[]{"Hryvnia", "UA", "Ukraine"});
        infoMap.put("UGX", new String[]{"Shilling", "UG", "Uganda"});
        infoMap.put("USD", new String[]{"US Dollar", "US", "United States"});
        infoMap.put("UYU", new String[]{"Peso", "UY", "Uruguay"});
        infoMap.put("UZS", new String[]{"Som", "UZ", "Uzbekistan"});
        infoMap.put("VEF", new String[]{"Bolivar", "VE", "Venezuela"});
        infoMap.put("VND", new String[]{"Dong", "VN", "Vietnam"});
        infoMap.put("VUV", new String[]{"Vatu", "VU", "Vanuatu"});
        infoMap.put("XAF", new String[]{"CFA Franc BEAC", "CF", "Central African Republic"});
        infoMap.put("XCD", new String[]{"East Caribbean Dollar", "AI", "Anguilla"});
        infoMap.put("XOF", new String[]{"CFA Franc BCEAO", "BJ", "Benin"});
        infoMap.put("XPF", new String[]{"CFP Franc", "PF", "French Polynesia"});
        infoMap.put("YER", new String[]{"Rial", "YE", "Yemen"});
        infoMap.put("ZAR", new String[]{"Rand", "ZA", "South Africa"});
        infoMap.put("ZMK", new String[]{"Kwacha", "ZM", "Zambia"});
        infoMap.put("ZWD", new String[]{"Zimbabwe Dollar", "ZW", "Zimbabwe"});
        return infoMap;
    }
}
