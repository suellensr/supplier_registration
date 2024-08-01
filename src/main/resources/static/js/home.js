$(document).ready(function() {
    // Verificar o token antes de carregar a página
    validateToken(function(isValid) {
        if (!isValid) {
            alert("Sessão expirada ou inválida. Por favor, faça login novamente.");
            localStorage.removeItem('token');
            window.location.href = '/login';
        }
    });

    loadSuppliers();

    // Botão de logout
    $('#logout').on('click', function() {
        localStorage.removeItem('token');
        window.location.href = '/login';
    });

    // Botão para cadastrar fornecedor
    $('#supplierRegisterBtn').on('click', function() {
        window.location.href = '/supplier-register';
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
            callback(false); // Token inválido ou expirado
        }
    });
}

function loadSuppliers() {
    $.ajax({
        url: '/get-suppliers', // Endpoint para obter os fornecedores
        type: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        success: function(data) {
            const supplierTableBody = $('#supplierTableBody');
            supplierTableBody.empty(); // Limpar a tabela antes de adicionar os dados

            data.forEach(supplier => {
                const supplierRow = `
                    <tr>
                        <td>${supplier.supplierName}</td>
                        <td>${supplier.contactName}</td>
                        <td>${supplier.contactEmail}</td>
                        <td>${supplier.documentNumber}</td>
                        <td>
                            <a class="btn btn-info btn-sm" href="/view-supplier/${supplier.id}"><i class="bi bi-eye"></i></a>
                            <a class="btn btn-warning btn-sm" href="/edit-supplier/${supplier.id}"><i class="bi bi-pencil"></i></a>
                            <button class="btn btn-danger btn-sm" onclick="deleteSupplier('${supplier.id}')"><i class="bi bi-trash"></i></button>
                        </td>
                    </tr>
                `;
                supplierTableBody.append(supplierRow);
            });
        },
        error: function(error) {
            console.error('Erro ao carregar fornecedores:', error);
            //window.location.href = '/login'; //Página de erros
        }
    });
}

function deleteSupplier(id) {
    console.log(localStorage.getItem("token"));
    if (confirm('Você tem certeza que quer deletar este fornecedor?')) {
        console.log(localStorage.getItem("token"));
        $.ajax({
            url: `/delete/${id}`,
            type: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function () {
                alert('Fornecedor deletado com sucesso!');
                loadSuppliers(); // Recarregar a lista de fornecedores
            },
            error: function (error) {
                console.error('Erro ao deletar fornecedor:', error);
            }
        });
    }
}