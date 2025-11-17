package kim.ics.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class CsvUtils {

    private CsvUtils() {
        throw new UnsupportedOperationException();
    }

    @SneakyThrows
    public static <T> List<T> listCsvRecord(@NonNull String cvsFilenameInResourceFolder, @NonNull Class<T> clazz) {

        InputStream inputStream = getInputStream(cvsFilenameInResourceFolder);
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(clazz)
                //.withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();

        return csvToBean.parse();
    }

    private static InputStream getInputStream(@NonNull String cvsFilenameInResourceFolder) {
        return CsvUtils.class.getClassLoader().getResourceAsStream(cvsFilenameInResourceFolder);
    }
}
