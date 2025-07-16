
$(document).ready(function () {

    // Slider setup
    const minPrice = document.getElementById('minPrice');
    const maxPrice = document.getElementById('maxPrice');
    const minPriceLabel = document.getElementById('minPriceLabel');
    const maxPriceLabel = document.getElementById('maxPriceLabel');
    const sliderTrack = document.querySelector('.slider-track');

    function formatCurrency(value) {
        return parseInt(value).toLocaleString('vi-VN') + '₫';
    }

    function updateSlider() {
        minPriceLabel.textContent = formatCurrency(minPrice.value);
        maxPriceLabel.textContent = formatCurrency(maxPrice.value);

        const minPercent = (minPrice.value / minPrice.max) * 1000;
        const maxPercent = (maxPrice.value / maxPrice.max) * 1000;

        minPriceLabel.style.left = `calc(${minPercent}% - 30px)`;
        maxPriceLabel.style.left = `calc(${maxPercent}% - 30px)`;

        sliderTrack.style.background = `linear-gradient(to right,
            #dee2e6 ${minPercent}%,
            #0d6efd ${minPercent}%,
            #0d6efd ${maxPercent}%,
            #dee2e6 ${maxPercent}%)`;
    }

    minPrice.addEventListener('input', function () {
        if (parseInt(minPrice.value) > parseInt(maxPrice.value)) {
            minPrice.value = maxPrice.value;
        }
        updateSlider();
    });

    maxPrice.addEventListener('input', function () {
        if (parseInt(maxPrice.value) < parseInt(minPrice.value)) {
            maxPrice.value = minPrice.value;
        }
        updateSlider();
    });

    updateSlider();

    const form = document.querySelector("#filterForm");
    const productListContainer = document.getElementById("productListContainer");
    function fetchProducts(formData) {
        const query = new URLSearchParams(formData);
        query.append("fragment", "true");

        fetch(`/products?${query.toString()}`)
            .then(res => res.text())
            .then(html => {
                productListContainer.innerHTML = html;
                attachPaginationEvents(); // Gắn lại sự kiện click
            })
            .catch(err => console.error("Error loading products:", err));
    }

    function attachPaginationEvents() {
        const links = document.querySelectorAll(".page-link-ajax");
        links.forEach(link => {
            link.addEventListener("click", function (e) {
                e.preventDefault();
                let page = this.getAttribute("data-page");

                const formData = new FormData(form);
                formData.set("page", page); // Set trang cần chuyển

                fetchProducts(formData);
            });
        });
    }

    // Khi người dùng submit form (bấm "Áp dụng")
    form.addEventListener("submit", function (e) {
        e.preventDefault(); // chặn reload
        const formData = new FormData(form);
        formData.set("page", 0); // reset về trang đầu
        fetchProducts(formData);
    });

    attachPaginationEvents();

});
