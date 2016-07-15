package com.sahaware.resysbni.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by PCnya-AINI on 30/05/2016.
 */
public class UtilityRupiahString {
    public static String formatRupiah(String nominal){
        String fixNominal = null;
        Locale locale = new Locale("id");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        fixNominal = "Rp " + fmt.format(Double.parseDouble(nominal)).substring(1);
        return fixNominal;
    }
}
