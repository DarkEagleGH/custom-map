package benchmark.jmh;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Utils {
    public static String sizeFormatter(long objectSize) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat sizeFormat = new DecimalFormat("###,###.###", symbols);
        return sizeFormat.format(objectSize);
    }
}
