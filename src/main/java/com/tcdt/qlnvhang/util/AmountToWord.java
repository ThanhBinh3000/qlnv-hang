package com.tcdt.qlnvhang.util;

import java.text.DecimalFormat;

public class AmountToWord {
    private static final String[] tensNames = {
            "",
            " mười",
            " hai mươi",
            " ba mươi",
            " bốn mươi",
            " năm mươi",
            " sáu mươi",
            " bảy mươi",
            " tám mươi",
            " chín mươi"
    };

    private static final String[] numNames = {
            "",
            " một",
            " hai",
            " ba",
            " bốn",
            " năm",
            " sáu",
            " bảy",
            " tám",
            " chín",
            " mười",
            " mười một",
            " mười hai",
            " mười ba",
            " mười bốn",
            " mười lăm",
            " mười sáu",
            " mười bảy",
            " mười tám",
            " mười chín"
    };

    private AmountToWord() {
    }

    private static String convertLessThanOneThousand(int number) {
        String soFar;

        if (number % 100 < 20) {
            soFar = numNames[number % 100];
            number /= 100;
        } else {
            soFar = numNames[number % 10];
            number /= 10;

            soFar = tensNames[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0) return soFar;
        return numNames[number] + " trăm" + soFar;
    }


    public static String convert(long number) {
        if (number == 0) {
            return "không";
        }
        String snumber = Long.toString(number);
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);
        int billions = Integer.parseInt(snumber.substring(0, 3));
        int millions = Integer.parseInt(snumber.substring(3, 6));
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        int thousands = Integer.parseInt(snumber.substring(9, 12));
        String tradBillions;
        switch (billions) {
            case 0:
                tradBillions = "";
                break;
            case 1:
                tradBillions = convertLessThanOneThousand(billions)
                        + " tỷ ";
                break;
            default:
                tradBillions = convertLessThanOneThousand(billions)
                        + " tỷ ";
        }
        String result = tradBillions;

        String tradMillions;
        switch (millions) {
            case 0:
                tradMillions = "";
                break;
            case 1:
                tradMillions = convertLessThanOneThousand(millions)
                        + " triệu ";
                break;
            default:
                tradMillions = convertLessThanOneThousand(millions)
                        + " triệu ";
        }
        result = result + tradMillions;

        String tradHundredThousands;
        switch (hundredThousands) {
            case 0:
                tradHundredThousands = "";
                break;
            case 1:
                tradHundredThousands = "một nghìn ";
                break;
            default:
                tradHundredThousands = convertLessThanOneThousand(hundredThousands)
                        + " nghìn ";
        }
        result = result + tradHundredThousands;
        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands);
        result = result + tradThousand + " đồng";
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }
}
