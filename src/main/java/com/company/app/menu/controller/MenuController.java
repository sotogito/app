package com.company.app.menu.controller;

import com.company.app.menu.dto.CategoryDto;
import com.company.app.menu.dto.MenuDto;
import com.company.app.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/menu")
@Controller
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/{menuCode}")
    public String menuDetail(@PathVariable int menuCode, Model model){
        MenuDto menu = menuService.findMenuByCode(menuCode);
        model.addAttribute("menu", menu);

        return "menu/detail";
    }

    /* 페이징 전
    @GetMapping("/list")
    public String menuList(Model model){

        List<MenuDto> menuList = menuService.findMenuList();
        model.addAttribute("menuList", menuList);

        return "menu/list";
    }
     */

    /*
        ## Pageable ##
        1. 페이징 처리에 필요한 정보(page, size, sort)를 처리하는 인터페이스
        2. Pageable 객체를 통해서 페이징 처리와 정렬을 동시에 처리할 수 있음
        3. 사용방법
           1) 페이징 처리에 필요한 정보를 따로 파라미터 전달받아 직접 생성하는 방법
              PageRequest.of(요청페이지번호, 조회할데이터건수, Sort객체)
           2) 정해진 파라미터(page, size, sort)로 전달받아 생성된 객체 바로 주입하는 방법
              @PageableDefault Pageable pageable
              => 따로 전달된 파라미터가 존재하지 않을 경우 기본값(페이지번호0, 조회할데이터건수10, 정렬기준없음)이 초기화됨
        4. 주의사항
           Pageable 인터페이스는 조회할 페이지번호를 0부터 인식
           => 넘어오는 페이지번호 파라미터를 -1 해야됨
     */

    // /menu/list?[page=xx]&[size=xx]&[sort=xxx,asc|desc]
    // 페이징 후
    @GetMapping("/list")
    public String menuList(@PageableDefault Pageable pageable, Model model){

        log.info("pageable: {}", pageable); // Pageable 매개변수에 page, size, sort 자동으로 바인딩 됨

        // * withPage() : 현재 Pageable의 기존설정(페이지size, 정렬 등)은 그대로 두고, 페이지 번호만 바꾼 "새로운 Pageable 객체를 반환"
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);

        if(pageable.getSort().isEmpty()){ // 정렬 파라미터가 존재하지 않을 경우 => 기본 정렬 기준 세우기
            // 정렬만 바꾸는건 따로 존재하지 않음 => 다시 새로 생성해야됨
            pageable = PageRequest.of(pageable.getPageNumber()
                                    , pageable.getPageSize()
                                    , Sort.by("menuCode").descending());
        }

        log.info("변경후 pageable: {}", pageable);

        Map<String, Object> map = menuService.findMenuList(pageable);
        model.addAttribute("menuList", map.get("menuList"));
        model.addAttribute("page", map.get("page"));
        model.addAttribute("beginPage", map.get("beginPage"));
        model.addAttribute("endPage", map.get("endPage"));
        model.addAttribute("isFirst", map.get("isFirst"));
        model.addAttribute("isLast", map.get("isLast"));

        return "menu/list";
    }

    @GetMapping("/regist")
    public void registPage(){}

    @ResponseBody
    @GetMapping(value="/categories", produces="application/json")
    public List<CategoryDto> categoryList(){
        return menuService.findCategoryList(); // '[{"categoryCode":xx, "categoryName":"xxx"}]'
    }

    @PostMapping("/regist")
    public String registMenu(MenuDto newMenu){
        menuService.registMenu(newMenu);
        return "redirect:/menu/list";
    }

    @GetMapping("/modify")
    public void modifyPage(int code, Model model){
        model.addAttribute("menu", menuService.findMenuByCode(code));
    }

    @PostMapping("/modify")
    public String modifyMenu(MenuDto modifyMenu){
        menuService.modifyMenu(modifyMenu);
        return "redirect:/menu/" + modifyMenu.getMenuCode();
    }

    @GetMapping("/remove")
    public String removeMenu(int code){
        menuService.removeMenu(code);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchMenu(String type, String query){

        List<MenuDto> menuList = new ArrayList<>();
        if("price".equals(type)){
            menuList = menuService.findMenuByMenuPrice(Integer.parseInt(query));
        }else if("name".equals(type)){
            menuList = menuService.findMenuByMenuName(query);
        }else if("both".equals(type)){ // String query = "10000,마늘";
            menuList = menuService.findMenuByPriceAndName(query.split(",")); // String[] = ["10000", "마늘"]

        }

        menuList.forEach(System.out::println);

        return "redirect:/";
    }

}
