let imageCounter = 0;

// (1) 업로드 영역 클릭 시 → 숨겨진 <input> 생성 + click()
function handleUploadAreaClick() {
  // 새 숨김 input 생성
  const newInput = document.createElement('input');
  newInput.type = 'file';
  newInput.name = 'files';
  newInput.accept = 'image/*';
  newInput.multiple = true;
  newInput.style.display = 'none'; // 화면에 보이지 않게

  // onchange 이벤트 → 미리보기 실행
  newInput.addEventListener('change', function() {
    previewFiles(newInput.files);
  });

  // 폼에 추가
  document.getElementById('fileInputs').appendChild(newInput);

  // 파일 선택 창 띄우기
  newInput.click();
}

// (2) 드래그&드롭 이벤트 처리
function handleDragOver(e) {
  e.preventDefault();
  e.stopPropagation();
}

function handleDrop(e) {
  e.preventDefault();
  e.stopPropagation();

  const files = e.dataTransfer.files; // 드롭된 파일들
  if (files && files.length > 0) {
    // 새 숨김 <input> 생성
    const newInput = document.createElement('input');
    newInput.type = 'file';
    newInput.name = 'files';
    newInput.accept = 'image/*';
    newInput.multiple = true;
    newInput.style.display = 'none';

    // DataTransfer 객체 활용
    const dt = new DataTransfer();
    for (let i = 0; i < files.length; i++) {
      dt.items.add(files[i]);
    }
    newInput.files = dt.files;

    // 미리보기 실행
    previewFiles(newInput.files);

    // 폼에 추가
    document.getElementById('fileInputs').appendChild(newInput);
  }
}

// (3) 파일 미리보기 기능
function previewFiles(fileList) {
  const previewContainer = document.getElementById('previewContainer');
  if (!fileList || fileList.length === 0) {
    return;
  }

  Array.from(fileList).forEach(file => {
    const reader = new FileReader();
    reader.onload = e => {
      imageCounter++;

      const imageWrapper = document.createElement('div');
      imageWrapper.classList.add('preview-wrapper');

      const numberLabel = document.createElement('div');
      numberLabel.classList.add('image-number');
      numberLabel.textContent = imageCounter;

      const img = document.createElement('img');
      img.src = e.target.result;
      img.classList.add('preview-image');

      // 모달 열기 이벤트
      img.addEventListener('click', () => {
        openModalImage(img.src);
      });

      imageWrapper.appendChild(numberLabel);
      imageWrapper.appendChild(img);
      previewContainer.appendChild(imageWrapper);
    };
    reader.readAsDataURL(file);
  });
}

// (4) 모달 관련 기능
function openModalImage(src) {
  $('#modalImage').attr('src', src);
  $('.modal-overlay').fadeIn(200);
}

function closeModal() {
  $('.modal-overlay').fadeOut(200);
}

// (5) DOM Ready 이벤트 처리
$(document).ready(function() {
  // 모달 닫기 이벤트
  $('.close-modal').on('click', function() {
    closeModal();
  });
  $('.modal-overlay').on('click', function(e) {
    if ($(e.target).hasClass('modal-overlay')) {
      closeModal();
    }
  });

  // 업로드 영역에 드래그&드롭 이벤트 설정
  const uploadArea = document.querySelector('.upload-area');
  if (uploadArea) {
    uploadArea.addEventListener('dragover', handleDragOver);
    uploadArea.addEventListener('drop', handleDrop);
    uploadArea.addEventListener('click', function() {
      handleUploadAreaClick();
    });
  }

  // 위치 변경 관련 이벤트 처리
  $("#locationInput").keyup(function(){
    var region = $(this).val();
    $.getJSON("region.get?region=" + region, function(regionData) {
      $(".region-list").empty();
      $.each(regionData.region, function(index, item) {
        $(".region-list").append("<li><a href='/region-get'>" + item.firstName + ", " + item.secondName + "</a></li>");
      });
    }).fail(function() {
      $(".region-list").empty();
      $(".region-list").append("<li>지역 정보를 불러오는 데 실패했습니다.</li>");
    });
  });

  $(document).on("click", ".region-list li", function(e) {
    e.preventDefault();
    e.stopPropagation();

    var name = $(this).text();
    var namearr = name.split(", ");
    $(".location-button").text(namearr[1]);

    $(".modal-overlay").fadeOut(200, function() {
      var fromValue = "reg";
      location.href = "/region-get?region=" + encodeURIComponent(namearr[0]) +
                      "&region2=" + encodeURIComponent(namearr[1]) +
                      "&from=" + encodeURIComponent(fromValue);
    });
  });
});