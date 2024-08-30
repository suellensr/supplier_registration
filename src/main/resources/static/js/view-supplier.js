$(document).ready(function() {
    // Verificar o token antes de carregar a página
    validateToken(function(isValid) {
        if (!isValid) {
            alert("Sessão expirada ou inválida. Por favor, faça login novamente.");
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
    });

    loadSupplierDetails();

    // Botão de logout
    $('#logout').on('click', function() {
        localStorage.removeItem('token');
        window.location.href = '/login';
    });
});

function validateToken(callback) {
    $.ajax({
        url: '/auth/validate-token',
        type: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        success: function(response) {
            callback(true); // Token válido
        },
        error: function(error) {
            console.error('Token validation error:', error); // Adicione um log para depuração
            callback(false); // Token inválido ou expirado
        }
    });
}

function loadSupplierDetails() {
    const supplierId = $('#supplierId').val();
    $.ajax({
        url: `/get-supplier/${supplierId}`,
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        success: function(data) {
            $('#supplierName').val(data.supplierName);
            $('#personType').val(data.personType);
            $('#documentNumber').val(data.documentNumber);
            applyDocumentMask();
            $('#activityDescription').val(data.activityDescription);

            $('#zipCode').val(data.address.zipCode);
            $('#street').val(data.address.street);
            $('#number').val(data.address.number);
            $('#complement').val(data.address.complement);
            $('#neighborhood').val(data.address.neighborhood);
            $('#city').val(data.address.city);
            $('#state').val(data.address.state);
            $('#country').val(data.address.country);

            $('#contactName').val(data.contactName);
            $('#contactEmail').val(data.contactEmail);


            $('#contactPhones').empty();
            data.phoneNumbers.forEach(function(phone, index) {
                $('#contactPhones').append(`
                    <input type="text" class="form-control phone-input mt-2" id="phone${index}" value="${phone}" readonly>
                `);
            });
            applyPhoneMasks();
        },
        error: function() {
            alert('Erro ao carregar detalhes do fornecedor.');
            window.location.href = '/home';
        }
    });
}

function deleteSupplier() {
    var supplierId = $('#supplierId').val(); // Obtém o ID do input oculto
    console.log('Supplier ID:', supplierId);

    if (confirm('Você tem certeza que quer deletar este fornecedor?')) {
        $.ajax({
            url: `/delete/${supplierId}`,
            type: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function () {
                window.location.href = '/home';
            },
            error: function (xhr) {
                console.error('Erro ao deletar fornecedor:', xhr.responseText);
                alert('Erro ao deletar fornecedor: ' + xhr.responseText);
            }
        });
    }
}

function editSupplier() {
    var supplierId = $('#supplierId').val(); // Obtém o ID do input oculto
    console.log('Supplier ID:', supplierId);

    window.location.href = '/edit-supplier/' + supplierId;
}


function applyDocumentMask() {
    $('#documentNumber').mask(function () {
        return $('#personType').val() === 'COMPANY' ? '00.000.000/0000-00' : '000.000.000-00';
    });
}

function applyPhoneMasks() {
    $('.phone-input').each(function() {
        var phoneValue = $(this).val();
        var cleanedPhone = phoneValue.replace(/\D/g, '');

        if (cleanedPhone.length === 13) {
            $(this).mask('+00 (00) 00000-0000');
        } else if (cleanedPhone.length === 12) {
            $(this).mask('+00 (00) 0000-0000');
        } else {
            $(this).unmask();
        }
    });
}