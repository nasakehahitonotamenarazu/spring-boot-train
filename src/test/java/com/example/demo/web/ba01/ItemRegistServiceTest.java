package com.example.demo.web.ba01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import com.example.demo.core.exception.AppException;
import com.example.demo.entity.Item;
import com.example.demo.web.mapper.ItemMapper;

/**
 * Item登録サービステスト
 */
@SpringBootTest
public class ItemRegistServiceTest {
    
    /** テスト対象、モックをInjectする対象 */
    @InjectMocks
    private ItemRegistService target;

    /** モックマッパー */
    @Mock
    private ItemMapper itemMapper;

    /**
     * 異常系テスト
     * 同一ItemNameの合計priceが3000円以上の場合、業務エラー
     */
    @Test
    public void testOverSumPrice() {
        //準備：モック仕込み
        List<Item> list = new ArrayList<>();
        list.add(new Item(1,"ペン", 1000, "CD-A01",LocalDate.now(), 0));
        list.add(new Item(2,"ペン", 1500, "CD-A01",LocalDate.now(),0));
        when(itemMapper.findAllByItemName(any())).thenReturn(list);
        //準備：引数作成
        Item item = new Item(5,"ペン", 1000, "CD-A01",LocalDate.now(), 0);
        
        //テスト対象呼び出し及び例外アサート
        AppException e = assertThrows(AppException.class, () -> target.registItem(item));
        //例外メッセージアサート
        assertEquals("ME001", e.getMessageId());
    }

    /**
     * 異常系テスト
     * キー重複エラー
     */
    @Test
    public void testDeplicate(){
        //準備：モック仕込み
        when(itemMapper.insertItem(any())).thenThrow(DuplicateKeyException.class);
        //準備：引数作成
        Item item = new Item(1,"ペン", 1000, "CD-A01",LocalDate.now(), 0);

        //テスト対象呼び出し及び例外アサート
        AppException e = assertThrows(AppException.class, () -> target.registItem(item));
        //例外メッセージアサート
        assertEquals("ME004", e.getMessageId());
    }

    /**
     * 正常系テスト
     */
    @Test
    public void testRegist(){
        //準備：引数作成
        Item item = new Item(2,"ペン", 1000, "CD-A01",LocalDate.of(2023, 7, 1), 0);
        //準備：マッパー引数のキャプチャー
        ArgumentCaptor<Item> capItem = ArgumentCaptor.forClass(Item.class);
        doReturn(1).when(itemMapper).insertItem(capItem.capture());

        //テスト対象呼び出し
        target.registItem(item);
        //マッパー引数のキャプチャー結果のアサート
        assertEquals(2, capItem.getValue().getId());
        assertEquals("ペン", capItem.getValue().getItemName());
        assertEquals(1000, capItem.getValue().getPrice());
        assertEquals("CD-A01", capItem.getValue().getGroupid());
        assertEquals(LocalDate.of(2023, 7, 1), capItem.getValue().getRegistDate());
        assertEquals(0, capItem.getValue().getVersionNo());
    }
}
