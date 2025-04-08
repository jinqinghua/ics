package kim.ics.util;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvUtils {

    private CsvUtils() {
        throw new UnsupportedOperationException();
    }

    @SneakyThrows
    public static <T> List<T> listCsvRecord(@NonNull String cvsFilenameInResourceFolder, @NonNull Class<T> clazz) {
        List<T> recordsList = new ArrayList<>();

        CSVParser csvParser = getCSVParser(cvsFilenameInResourceFolder);
        for (CSVRecord csvRecord : csvParser) {
            T instance = BeanUtils.instantiateClass(clazz);
            for (Field field : clazz.getDeclaredFields()) {
                CsvHeader annotation = AnnotationUtils.findAnnotation(field, CsvHeader.class);
                if (annotation != null) {
                    String headerName = annotation.name();
                    String value = csvRecord.get(headerName);
                    field.setAccessible(true);
                    field.set(instance, value);
                }
            }
            recordsList.add(instance);
        }
        return recordsList;
    }

    @SneakyThrows
    public static CSVParser getCSVParser(@NonNull String cvsFilenameInResourceFolder) {
        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader()  // 让第一行作为 Header
                //.setSkipHeaderRecord(true)  // 跳过第一行 (Header)
                .setIgnoreSurroundingSpaces(true)  // 忽略单元格两侧的空格
                .setIgnoreEmptyLines(true)  // 忽略空行
                .get();

        return CSVParser.parse(getInputStream(cvsFilenameInResourceFolder), StandardCharsets.UTF_8, csvFormat);
    }

    private static InputStream getInputStream(@NonNull String cvsFilenameInResourceFolder) {
        return CsvUtils.class.getClassLoader().getResourceAsStream(cvsFilenameInResourceFolder);
    }
}
