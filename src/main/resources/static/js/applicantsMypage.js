document.addEventListener("DOMContentLoaded", function () {
    const popup = document.querySelector(".tag-edit-popup");
    const editTagButton = document.querySelector(".edit-tag");
    const closeButton = document.querySelector(".close-button");
    const header = document.querySelector(".title-popup");
    const addTagButton = document.querySelector(".add-tag-button");
    const tagInput = document.querySelector(".tag-input");
    const tagList = document.querySelector(".tag-list");
    const saveButton = document.querySelector(".save-button");
    const tagGroup = document.querySelector(".tag-group");
    const allTagsButton = document.querySelector(".tag.all");

    let isDragging = false;
    let offsetX, offsetY;
    let showAll = false; // 전체보기 상태를 저장하는 변수

    // 관심태그 수정 버튼 클릭 시 팝업 열기
    editTagButton.addEventListener("click", function () {
        popup.style.display = "block";
    });

    // close-button 클릭 시 팝업 닫기
    closeButton.addEventListener("click", function () {
        popup.style.display = "none";
    });

    // 팝업 드래그 이동
    header.addEventListener("mousedown", function (e) {
        isDragging = true;
        offsetX = e.clientX - popup.getBoundingClientRect().left;
        offsetY = e.clientY - popup.getBoundingClientRect().top;
        document.body.style.userSelect = "none"; // 텍스트 선택 방지
    });

    document.addEventListener("mousemove", function (e) {
        if (!isDragging) return;
        const x = e.clientX - offsetX;
        const y = e.clientY - offsetY;
        popup.style.left = `${x}px`;
        popup.style.top = `${y}px`;
        popup.style.position = "absolute";
    });

    document.addEventListener("mouseup", function () {
        isDragging = false;
        document.body.style.userSelect = "auto"; // 텍스트 선택 허용
    });

    // 관심태그 추가 버튼 클릭 시 태그 목록에 추가
    addTagButton.addEventListener("click", function () {
        const tagText = tagInput.value.trim();
        if (tagText) {
            // 중복 태그 검사
            const existingTags = Array.from(tagList.querySelectorAll(".tag-item")).map(tag => tag.textContent.replace("\u00d7", "").trim());
            if (existingTags.includes(tagText)) {
                alert("이미 존재하는 태그입니다."); // 중복 시 경고 메시지
                return;
            }

            // 최대 태그 수 검사 (20개)
            if (existingTags.length >= 20) {
                alert("최대 20개의 관심태그만 추가할 수 있습니다.");
                return;
            }

            const newTag = document.createElement("div");
            newTag.classList.add("tag-item");
            newTag.innerHTML = `${tagText} <span class="remove-tag">&times;</span>`;
            tagList.appendChild(newTag);
            tagInput.value = "";
        }
    });

    // 태그 삭제 버튼 클릭 시 태그 삭제
    tagList.addEventListener("click", function (e) {
        if (e.target.classList.contains("remove-tag")) {
            const tagItem = e.target.parentElement;
            tagList.removeChild(tagItem);
        }
    });

    // 관심태그 저장하기 버튼 클릭 시 서버에 태그 저장
    saveButton.addEventListener("click", function () {
        saveTagsToDB(); // 태그를 서버에 저장
        popup.style.display = "none";
    });

    // 관심태그 전체보기 버튼 클릭 시 전체 태그 보기 토글
    allTagsButton.addEventListener("click", function () {
        showAll = !showAll; // 상태 토글
        updateTagGroup();

        if (showAll) {
            allTagsButton.textContent = "관심태그 닫기";
        } else {
            allTagsButton.textContent = "관심태그 전체보기";
        }
    });

    // 태그 그룹 업데이트 함수
    function updateTagGroup() {
        tagGroup.innerHTML = ""; // 기존 태그 초기화
        const tags = tagList.querySelectorAll(".tag-item");
        tags.forEach((tag, index) => {
            // 전체보기가 아닐 때 처음 20개만 표시
            if (!showAll && index >= 20) return;
            const button = document.createElement("button");
            button.classList.add("tag");
            button.textContent = tag.textContent.replace("\u00d7", "").trim();
            tagGroup.appendChild(button);
        });

        if (showAll) {
            tagGroup.classList.add("show-all");
        } else {
            tagGroup.classList.remove("show-all");
        }
    }

    // 태그 그룹 업데이트 후 서버에 저장하는 함수
    function saveTagsToDB() {
        const userTags = Array.from(tagList.querySelectorAll(".tag-item")).map(tag => tag.textContent.replace("\u00d7", "").trim());

        // 유효성 검사
        if (userTags.length > 20) {
            alert("최대 20개의 관심태그만 저장할 수 있습니다.");
            return;
        }

        // AJAX 요청을 통해 서버에 태그 저장 (POST /usertags/update)
        fetch(contextPath + "/usertags/update", { // 컨텍스트 패스 사용
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ tags: userTags })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.status === "success") {
                // 태그가 성공적으로 저장되었으면 메인 페이지의 태그 그룹을 업데이트
                tagGroup.innerHTML = "";
                data.userTags.forEach(tag => {
                    const button = document.createElement("button");
                    button.classList.add("tag");
                    button.textContent = tag.tagName;
                    tagGroup.appendChild(button);
                });
                alert("관심태그가 성공적으로 저장되었습니다.");
            } else {
                alert(`관심태그 저장에 실패했습니다: ${data.message}`);
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("관심태그 저장 중 오류가 발생했습니다.");
        });
    }

    // 초기 로드 시 태그 20개만 표시
    updateTagGroup();
});  
