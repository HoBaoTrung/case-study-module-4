function refreshCartView() {
    $.get("/carts/fragment", function (html) {
        $("#cart-container").html(html);
    });
}

// AJAX xử lý cộng/trừ số lượng
function updateCart(button, type) {
    const productId = $(button).attr("data-id");

    $.ajax({
        url: '/carts',
        method: 'PUT',
        contentType: 'application/json', // gửi kiểu JSON
        data: JSON.stringify({
            type: type,         // "add" hoặc "sub"
            id: productId
        }),
        success: function () {
            refreshCartView();
        },
        error: function () {
            swal("Lỗi", "Không thể cập nhật giỏ hàng", "error");
        }
    });
}


// AJAX xử lý xóa sản phẩm
$(document).ready(function () {
    $(document).on('click', '.btn-delete', function () {

        const productId = $(this).attr("data-id");

        const productName = $(this).attr("data-name");

        swal({
            title: "Xóa sản phẩm ra khỏi giỏ hàng?",
            text: productName,
            icon: "warning",
            buttons: ["Hủy", "Xóa"],
            dangerMode: true,
        }).then((willDelete) => {
            if (willDelete) {
                $.ajax({
                    url: '/carts?id=' + productId,
                    method: 'DELETE',
                    // data: { id: productId },
                    success: function () {
                        refreshCartView();
                    },
                    error: function () {
                        swal("Lỗi", "Không thể xóa sản phẩm", "error");
                    }
                });
            }
        });
    });
});