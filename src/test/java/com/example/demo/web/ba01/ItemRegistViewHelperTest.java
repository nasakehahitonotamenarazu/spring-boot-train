package com.example.demo.web.ba01;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemRegistViewHelperTest {

    @InjectMocks
    private ItemRegistViewHelper itemRegistViewHelper;
    
    /**
     * 4ケース分をパラメータイズドテストとして一度に実施する
     */
    @ParameterizedTest
    @CsvSource({
        "CD-A01, 文具",
        "CD-A02, その他",
        "same, ",
        " , "
    })
    public void test(String groupid, String label) {
        assertEquals(label, itemRegistViewHelper.getGroupName(groupid));
    }
}
