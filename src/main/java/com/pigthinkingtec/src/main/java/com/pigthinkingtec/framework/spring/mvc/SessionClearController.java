/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pigthinkingtec.framework.spring.mvc;

import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Sessionに入っているFormObjectを削除するコントローラクラス
 * メニューへ戻る処理に利用する。
 * frameworkPropertiesのclearSession.pageにメニュー画面のjspを指定しておく
 * 
 * 
 * @author yizhou
 */
@Controller
public class SessionClearController extends AbstractController {
    
    @Autowired
    private Properties frameworkProperties;
    
    @RequestMapping(value="clearSession")
    public String clearSession(Model model, HttpServletRequest request) {
        
        clearFormFromSession(request);
        
        String returnPage = frameworkProperties.getProperty("clearSession.page");
        
        return returnPage;
    }
    
}
