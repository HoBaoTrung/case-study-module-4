$(document).ready(function () {
// Add to cart (event delegation)
    $(document).on('click', '.add-to-cart', function () {
        const productId = $(this).data('id');
        const productName = $(this).data('name');

        $.ajax({
            url: '/carts',
            type: 'POST',
            data: {
                action: 'add',
                id: productId
            },
            success: function (response) {
                swal({
                    title: "Thêm thành công",
                    text: productName,
                    icon: "success"
                });
            },
            error: function () {
                alert('Thêm thất bại. Vui lòng thử lại.');
            }
        });
    });

    // delete product (event delegation)
    $(document).on('click', '.delete-product', function () {
        const productId = $(this).data('id');
        const productName = $(this).data('name');

        swal({
            title: "Xóa sản phẩm khỏi cửa hàng?",
            text: productName,
            icon: "warning",
            buttons: ["Hủy", "Xóa"],
            dangerMode: true,
        }).then((willDelete) => {
            if (willDelete) {
                $.ajax({
                    url: '/products?id=' + productId,
                    method: 'DELETE',
                    success: function () {
                        window.location.href = '/products';
                    },
                    error: function () {
                        swal("Lỗi", "Không thể xóa sản phẩm", "error");
                    }
                });
            }
        });
    });


});