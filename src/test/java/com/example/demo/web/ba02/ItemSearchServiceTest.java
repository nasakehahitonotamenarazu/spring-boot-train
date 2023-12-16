package com.example.demo.web.ba02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.demo.core.exception.AppException;
import com.example.demo.entity.Item;
import com.example.demo.web.mapper.ItemMapper;

/**
 * Item検索サービステスト
 */
@SpringBootTest
public class ItemSearchServiceTest {
    
    /** テスト対象、モックをInjectする対象 */
    @InjectMocks
    private ItemSearchService target;

    /** モックマッパー */
    @Mock
    private ItemMapper itemMapper;

    /**
     * 異常系テスト
     * 検索結果が無く、ゼロ件エラーが発生すること
     */
    @Test
    public void testCountZero() {
        //準備：モック仕込み
        when(itemMapper.countAll(any())).thenReturn(0L);
        //準備：引数作成
        ItemSearchCriteria criteria = new ItemSearchCriteria("aaa", null, PageRequest.of(0, 3));

        //テスト対象呼び出し及び例外アサート
        AppException e = assertThrows(AppException.class, () -> target.findAll(criteria));
        //例外メッセージアサート
        assertEquals("ME003", e.getMessageId());
    }

    /**
     * 異常系テスト
     * 検索結果が1001件以上ヒットで、1000件超過エラーが発生すること
     */
    @Test
    public void testCountThousand() {
        //準備：モック仕込み
        when(itemMapper.countAll(any())).thenReturn(1001L);
        //準備：引数作成
        ItemSearchCriteria criteria = new ItemSearchCriteria("aaa", null, PageRequest.of(0, 3));
        
        //テスト対象呼び出し及び例外アサート
        AppException e = assertThrows(AppException.class, () -> target.findAll(criteria));
        //例外メッセージアサート
        assertEquals("ME002", e.getMessageId());
    }

    /**
     * 正常系テスト
     * 1件ヒットすること、ヒットした1件の内容が期待通りであること
     */
    @Test
    public void testCountOne() {
        //準備：モック仕込み
        when(itemMapper.countAll(any())).thenReturn(1L);
        //準備：モック仕込み
        List<Item> list = new ArrayList<>();
        list.add(new Item(1, "ペン", 100, "CD-A01", LocalDate.now(), 0));
        when(itemMapper.findAll(any())).thenReturn(list);
        //準備：引数作成
        ItemSearchCriteria criteria = new ItemSearchCriteria(null, null, PageRequest.of(0, 5));

        //テスト対象呼び出し
        Page<Item> pages = target.findAll(criteria);
        //アサート
        assertEquals(0,pages.getPageable().getPageNumber());
        assertEquals(1,pages.getTotalPages());
        assertEquals(1, pages.getContent().get(0).getId());
        assertEquals("ペン", pages.getContent().get(0).getItemName());
        assertEquals(100, pages.getContent().get(0).getPrice());
        assertEquals("CD-A01", pages.getContent().get(0).getGroupid());
        assertEquals(0, pages.getContent().get(0).getVersionNo());
    }


}
