/* header.jsp */
// 햄버거 버튼 전용 (전체 메뉴 테이블 온오프)
try {
    const menuButton = document.getElementById("menuButton");
    const menuImg = menuButton.querySelector("img");
    const menuDrop = document.getElementById("header-dropdown");
    const dropTable = menuDrop.querySelector("table");
    const dropUls = menuDrop.querySelectorAll("ul");

    function changeMenuImg(event) {
        switch (event.type) {
            case 'mouseenter':
                menuImg.src = contextPath + "/img/btn_menu_on.png";
                break;
            case 'mouseleave':
                if (!menuButton.classList.contains('On')) {
                    menuImg.src = contextPath + "/img/btn_menu.png";
                }
                break;
            case 'click' :
                if (!menuButton.classList.contains('On')) {
                    menuButton.classList.add('On');
                    menuImg.src = contextPath + "/img/btn_menu_on.png";
                    menuDrop.style.display = "flex";
                    dropTable.style.display = "table";
                    dropUls.forEach((ul) => {
                        ul.style.display = "none";
                    });
                } else {
                    menuButton.classList.remove('On');
                    menuImg.src = contextPath + "/img/btn_menu.png";
                    menuDrop.style.display = "none";
                    dropTable.style.display = "none";
                }
                break;
        }
    }

    menuButton.addEventListener('mouseenter', changeMenuImg);
    menuButton.addEventListener('mouseleave', changeMenuImg);
    menuButton.addEventListener('click', changeMenuImg);
} catch (error) {
    console.log("heder-menu: ", error);
}

// 소메뉴
function dropMenu(index) {

}

// 메뉴탭 활성화
try {
    function activeMenu(activeNo) {
        const menuItems = document.querySelectorAll("#header-nav-bottom .menu > li");
        menuItems[activeNo].classList.add("On");
    }
} catch(error) {
    console.log("activeMenu: ", error);
}


/* header_biz.jsp */
// 메뉴 활성화
function activeMenuBiz(activeBizNo) {
    const menuItems = document.querySelectorAll("#biz-nav-left > li");
    menuItems[activeBizNo].classList.add("On");
}


document.addEventListener("DOMContentLoaded", function() {
try {
    activeMenu(activeNo);
} catch(error) {
    console.log("activeMenu: ", error);
}

    try {
        activeMenuBiz(activeBizNo);
    } catch(error) {
        console.log("activeMenuBiz: ", error);
    }
});