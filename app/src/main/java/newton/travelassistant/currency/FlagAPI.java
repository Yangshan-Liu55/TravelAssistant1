package newton.travelassistant.currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlagAPI {

    public String getFlagURL(String currencyCode) {
        if (new FlagAPI().getInfo(currencyCode) != null) {
            String countryCode = new FlagAPI().getInfo(currencyCode)[1].toLowerCase();
            return "https://www.countryflags.io/" + countryCode + "/flat/64.png";
        } else {
            return "";
        }
    }

    public String getCurrencyFullName(String currencyCode) {
        if (new FlagAPI().getInfo(currencyCode) != null) {
            return new FlagAPI().getInfo(currencyCode)[0];
        } else {
            return "";
        }
    }

    public String[] getInfo(String currencyCode) {
        Map<String, String[]> infoMap = init();

        if (infoMap.get(currencyCode) != null) {
            return infoMap.get(currencyCode);
        } else {
            return null;
        }
    }

    public List<String> getAllCurrencyCode() {
        Map<String, String[]> infoMap = init();
        List<String> allKeys = new ArrayList<>(infoMap.keySet());
        return allKeys;
    }

    public Map<String, String[]> init() {
        Map<String, String[]> infoMap = new HashMap<>();
//        infoMap.put("currencyCode", new String[]{"currencyName", "countryCode"});

        infoMap.put("AED", new String[]{"Emirati Dirham", "AE"});
        infoMap.put("AFN", new String[]{"Afghan Afghani", "AF"});
        infoMap.put("ALL", new String[]{"Albanian Lek", "AL"});
        infoMap.put("AMD", new String[]{"Armenian Dram", "AM"});
        infoMap.put("ANG", new String[]{"Dutch Guilder", "AN"});
        infoMap.put("AOA", new String[]{"Angolan Kwanza", "AO"});
        infoMap.put("ARS", new String[]{"Argentine Peso", "AR"});
        infoMap.put("AUD", new String[]{"Australian Dollar", "AU"});
        infoMap.put("AWG", new String[]{"Aruban or Dutch Guilder", "AW"});
        infoMap.put("AZN", new String[]{"Azerbaijan Manat", "AZ"});
        infoMap.put("BAM", new String[]{"Bosnian Convertible Mark", "BA"});
        infoMap.put("BBD", new String[]{"Barbadian or Bajan Dollar", "BB"});
        infoMap.put("BDT", new String[]{"Bangladeshi Taka", "BD"});
        infoMap.put("BGN", new String[]{"Bulgarian Lev", "BG"});
        infoMap.put("BHD", new String[]{"Bahraini Dinar", "BH"});
        infoMap.put("BIF", new String[]{"Burundian Franc", "BI"});
        infoMap.put("BMD", new String[]{"Bermudian Dollar", "BM"});
        infoMap.put("BND", new String[]{"Bruneian Dollar", "BN"});
        infoMap.put("BOB", new String[]{"Bolivian Bolíviano", "BO"});
        infoMap.put("BRL", new String[]{"Brazilian Real", "BR"});
        infoMap.put("BSD", new String[]{"Bahamian Dollar", "BS"});
        infoMap.put("BTN", new String[]{"Bhutanese Ngultrum", "BT"});
        infoMap.put("BWP", new String[]{"Botswana Pula", "BW"});
        infoMap.put("BZD", new String[]{"Belizean Dollar", "BZ"});
        infoMap.put("CAD", new String[]{"Canadian Dollar", "CA"});
        infoMap.put("CDF", new String[]{"Congolese Franc", "CD"});
        infoMap.put("CHF", new String[]{"Swiss Franc", "CH"});
        infoMap.put("CLP", new String[]{"Chilean Peso", "CL"});
        infoMap.put("CNY", new String[]{"Chinese Yuan Renminbi", "CN"});
        infoMap.put("COP", new String[]{"Colombian Peso", "CO"});
        infoMap.put("CRC", new String[]{"Costa Rican Colon", "CR"});
        infoMap.put("CUC", new String[]{"Cuban Convertible Peso", "CU"});
        infoMap.put("CUP", new String[]{"Cuban Peso", "CU"});
        infoMap.put("CVE", new String[]{"Cape Verdean Escudo", "CV"});
        infoMap.put("CZK", new String[]{"Czech Koruna", "CZ"});
        infoMap.put("DJF", new String[]{"Djiboutian Franc", "DJ"});
        infoMap.put("DKK", new String[]{"Danish Krone", "DK"});
        infoMap.put("DOP", new String[]{"Dominican Peso", "DO"});
        infoMap.put("DZD", new String[]{"Algerian Dinar", "DZ"});
        infoMap.put("EGP", new String[]{"Egyptian Pound", "EG"});
        infoMap.put("ERN", new String[]{"Eritrean Nakfa", "ER"});
        infoMap.put("ETB", new String[]{"Ethiopian Birr", "ET"});
        infoMap.put("EUR", new String[]{"Euro", "EU"});
        infoMap.put("FJD", new String[]{"Fijian Dollar", "FJ"});
        infoMap.put("FKP", new String[]{"Falkland Island Pound", "FK"});
        infoMap.put("GBP", new String[]{"British Pound", "GB"});
        infoMap.put("GEL", new String[]{"Georgian Lari", "GE"});
        infoMap.put("GGP", new String[]{"Guernsey Pound", "GG"});
        infoMap.put("GHS", new String[]{"Ghanaian Cedi", "GH"});
        infoMap.put("GIP", new String[]{"Gibraltar Pound", "GI"});
        infoMap.put("GMD", new String[]{"Gambian Dalasi", "GM"});
        infoMap.put("GNF", new String[]{"Guinean Franc", "GN"});
        infoMap.put("GTQ", new String[]{"Guatemalan Quetzal", "GT"});
        infoMap.put("GYD", new String[]{"Guyanese Dollar", "GY"});
        infoMap.put("HKD", new String[]{"Hong Kong Dollar", "HK"});
        infoMap.put("HNL", new String[]{"Honduran Lempira", "HN"});
        infoMap.put("HRK", new String[]{"Croatian Kuna", "HR"});
        infoMap.put("HTG", new String[]{"Haitian Gourde", "HT"});
        infoMap.put("HUF", new String[]{"Hungarian Forint", "HU"});
        infoMap.put("IDR", new String[]{"Indonesian Rupiah", "ID"});
        infoMap.put("ILS", new String[]{"Israeli Shekel", "IL"});
        infoMap.put("INR", new String[]{"Indian Rupee", "IN"});
        infoMap.put("IQD", new String[]{"Iraqi Dinar", "IQ"});
        infoMap.put("IRR", new String[]{"Iranian Rial", "IR"});
        infoMap.put("ISK", new String[]{"Icelandic Krona", "IS"});
        infoMap.put("JMD", new String[]{"Jamaican Dollar", "JM"});
        infoMap.put("JOD", new String[]{"Jordanian Dinar", "JO"});
        infoMap.put("JPY", new String[]{"Japanese Yen", "JP"});
        infoMap.put("KES", new String[]{"Kenyan Shilling", "KE"});
        infoMap.put("KGS", new String[]{"Kyrgyzstani Som", "KG"});
        infoMap.put("KHR", new String[]{"Cambodian Riel", "KH"});
        infoMap.put("KMF", new String[]{"Comorian Franc", "KM"});
        infoMap.put("KPW", new String[]{"North Korean Won", "KP"});
        infoMap.put("KRW", new String[]{"South Korean Won", "KR"});
        infoMap.put("KWD", new String[]{"Kuwaiti Dinar", "KW"});
        infoMap.put("KYD", new String[]{"Caymanian Dollar", "KY"});
        infoMap.put("KZT", new String[]{"Kazakhstani Tenge", "KZ"});
        infoMap.put("LAK", new String[]{"Lao Kip", "LA"});
        infoMap.put("LBP", new String[]{"Lebanese Pound", "LB"});
        infoMap.put("LKR", new String[]{"Sri Lankan Rupee", "LK"});
        infoMap.put("LRD", new String[]{"Liberian Dollar", "LR"});
        infoMap.put("LSL", new String[]{"Basotho Loti", "LS"});
        infoMap.put("LYD", new String[]{"Libyan Dinar", "LY"});
        infoMap.put("MAD", new String[]{"Moroccan Dirham", "MA"});
        infoMap.put("MDL", new String[]{"Moldovan Leu", "MD"});
        infoMap.put("MGA", new String[]{"Malagasy Ariary", "MG"});
        infoMap.put("MKD", new String[]{"Macedonian Denar", "MK"});
        infoMap.put("MMK", new String[]{"Burmese Kyat", "MM"});
        infoMap.put("MNT", new String[]{"Mongolian Tughrik", "MN"});
        infoMap.put("MOP", new String[]{"Macau Pataca", "MO"});
        infoMap.put("MUR", new String[]{"Mauritian Rupee", "MU"});
        infoMap.put("MVR", new String[]{"Maldivian Rufiyaa", "MV"});
        infoMap.put("MWK", new String[]{"Malawian Kwacha", "MW"});
        infoMap.put("MXN", new String[]{"Mexican Peso", "MX"});
        infoMap.put("MYR", new String[]{"Malaysian Ringgit", "MY"});
        infoMap.put("MZN", new String[]{"Mozambican Metical", "MZ"});
        infoMap.put("NAD", new String[]{"Namibian Dollar", "NA"});
        infoMap.put("NGN", new String[]{"Nigerian Naira", "NG"});
        infoMap.put("NIO", new String[]{"Nicaraguan Cordoba", "NI"});
        infoMap.put("NOK", new String[]{"Norwegian Krone", "NO"});
        infoMap.put("NPR", new String[]{"Nepalese Rupee", "NP"});
        infoMap.put("NZD", new String[]{"New Zealand Dollar", "NZ"});
        infoMap.put("OMR", new String[]{"Omani Rial", "OM"});
        infoMap.put("PAB", new String[]{"Panamanian Balboa", "PA"});
        infoMap.put("PEN", new String[]{"Peruvian Sol", "PE"});
        infoMap.put("PGK", new String[]{"Papua New Guinean Kina", "PG"});
        infoMap.put("PHP", new String[]{"Philippine Peso", "PH"});
        infoMap.put("PKR", new String[]{"Pakistani Rupee", "PK"});
        infoMap.put("PLN", new String[]{"Polish Zloty", "PL"});
        infoMap.put("PYG", new String[]{"Paraguayan Guarani", "PY"});
        infoMap.put("QAR", new String[]{"Qatari Riyal", "QA"});
        infoMap.put("RON", new String[]{"Romanian Leu", "RO"});
        infoMap.put("RSD", new String[]{"Serbian Dinar", "RS"});
        infoMap.put("RUB", new String[]{"Russian Ruble", "RU"});
        infoMap.put("RWF", new String[]{"Rwandan Franc", "RW"});
        infoMap.put("SAR", new String[]{"Saudi Arabian Riyal", "SA"});
        infoMap.put("SBD", new String[]{"Solomon Islander Dollar", "SB"});
        infoMap.put("SCR", new String[]{"Seychellois Rupee", "SC"});
        infoMap.put("SDG", new String[]{"Sudanese Pound", "SD"});
        infoMap.put("SEK", new String[]{"Swedish Krona", "SE"});
        infoMap.put("SGD", new String[]{"Singapore Dollar", "SG"});
        infoMap.put("SHP", new String[]{"Saint Helenian Pound", "SH"});
        infoMap.put("SLL", new String[]{"Sierra Leonean Leone", "SL"});
        infoMap.put("SOS", new String[]{"Somali Shilling", "SO"});
        infoMap.put("SRD", new String[]{"Surinamese Dollar", "SR"});
        infoMap.put("SVC", new String[]{"Salvadoran Colon", "SV"});
        infoMap.put("SYP", new String[]{"Syrian Pound", "SY"});
        infoMap.put("SZL", new String[]{"Swazi Lilangeni", "SZ"});
        infoMap.put("THB", new String[]{"Thai Baht", "TH"});
        infoMap.put("TJS", new String[]{"Tajikistani Somoni", "TJ"});
        infoMap.put("TMT", new String[]{"Turkmenistani Manat", "TM"});
        infoMap.put("TND", new String[]{"Tunisian Dinar", "TN"});
        infoMap.put("TOP", new String[]{"Tongan Pa'anga", "TO"});
        infoMap.put("TRY", new String[]{"Turkish Lira", "TR"});
        infoMap.put("TTD", new String[]{"Trinidadian Dollar", "TT"});
        infoMap.put("TWD", new String[]{"Taiwan New Dollar", "TW"});
        infoMap.put("TZS", new String[]{"Tanzanian Shilling", "TZ"});
        infoMap.put("UAH", new String[]{"Ukrainian Hryvnia", "UA"});
        infoMap.put("UGX", new String[]{"Ugandan Shilling", "UG"});
        infoMap.put("USD", new String[]{"US Dollar", "US"});
        infoMap.put("UYU", new String[]{"Uruguayan Peso", "UY"});
        infoMap.put("UZS", new String[]{"Uzbekistani Som", "UZ"});
        infoMap.put("VEF", new String[]{"Venezuelan Bolívar", "VE"});
        infoMap.put("VND", new String[]{"Vietnamese Dong", "VN"});
        infoMap.put("VUV", new String[]{"Ni-Vanuatu Vatu", "VU"});
        infoMap.put("WST", new String[]{"Samoan Tala", "WS"});
        infoMap.put("XAF", new String[]{"Central African CFA Franc BEAC", "CF"});
        infoMap.put("XCD", new String[]{"East Caribbean Dollar", "AI"});
        infoMap.put("XOF", new String[]{"CFA Franc", "BJ"});
        infoMap.put("XPF", new String[]{"CFP Franc", "PF"});
        infoMap.put("YER", new String[]{"Yemeni Rial", "YE"});
        infoMap.put("ZAR", new String[]{"South African Rand", "ZA"});
        infoMap.put("ZMW", new String[]{"Zambian Kwacha", "ZM"});
        infoMap.put("ZWL", new String[]{"Zimbabwe Dollar", "ZW"});

        return infoMap;
    }
}
