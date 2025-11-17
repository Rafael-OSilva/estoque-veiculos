// ================= CONFIG ==================
const API_BASE_URL = 'http://localhost:8080/api';

let state = {
    veiculos: [],
    marcas: [],
    modelos: [],
    filtros: { texto: "", status: "" }
};

// ================= INICIALIZAÇÃO ==================
document.addEventListener('DOMContentLoaded', () => {
    configurarNavegacao();
    carregarDadosIniciais();
});

// Navegação
function configurarNavegacao() {
    const navLinks = document.querySelectorAll('.nav-link');

    navLinks.forEach(link => {
        link.addEventListener('click', e => {
            e.preventDefault();

            navLinks.forEach(l => l.classList.remove('active'));
            link.classList.add('active');

            mostrarSecao(link.dataset.section);
        });
    });
}

function mostrarSecao(id) {
    document.querySelectorAll('.section').forEach(sec => sec.classList.remove('active'));
    document.getElementById(id).classList.add('active');

    if (id === 'veiculos') carregarVeiculos();
    if (id === 'marcas') carregarMarcas();
    if (id === 'modelos') carregarModelos();
    if (id === 'relatorios') carregarRelatorios();
}

// ================= CARREGAMENTO ==================
async function carregarDadosIniciais() {
    // Carrega marcas e modelos primeiro, para que os selects de veículo funcionem
    await carregarMarcas();
    await carregarModelos();
    await carregarVeiculos();
}

async function carregarVeiculos() {
    try {
        showLoading('listaVeiculos');

        const response = await fetch(`${API_BASE_URL}/veiculos`);
        if (!response.ok) throw new Error("Erro ao carregar veículos");

        state.veiculos = await response.json();
        exibirVeiculos(state.veiculos);

    } catch (err) {
        showError("listaVeiculos", "Erro ao carregar veículos");
        console.error(err);
    }
}

async function carregarMarcas() {
    try {
        const response = await fetch(`${API_BASE_URL}/marcas`);
        state.marcas = await response.json();
        exibirMarcas(state.marcas);
    } catch (err) {
        console.error("Erro ao carregar marcas:", err);
    }
}

async function carregarModelos() {
    try {
        const response = await fetch(`${API_BASE_URL}/modelos`);
        state.modelos = await response.json();
        exibirModelos(state.modelos);
    } catch (err) {
        console.error("Erro ao carregar modelos:", err);
    }
}

// ================= EXIBIÇÃO ==================
function exibirVeiculos(lista) {
    const container = document.getElementById('listaVeiculos');

    if (!lista.length) {
        container.innerHTML = `<div class="loading">Nenhum veículo encontrado</div>`;
        return;
    }

    container.innerHTML = lista.map(v => {
        // --- CORREÇÃO AQUI ---
        // A marca agora é acessada através do modelo: v.modelo.marca.nome
        const marcaNome = v.modelo?.marca?.nome || "N/A";
        const modeloNome = v.modelo?.nome || "N/A";

        return `
        <div class="card ${v.status.toLowerCase()}">
            <div class="card-header">
                <div class="card-title">${marcaNome} ${modeloNome}</div>
                <span class="card-status status-${v.status.toLowerCase()}">${v.status}</span>
            </div>

            <div class="card-details">
                <div class="card-detail"><strong>Marca/Modelo:</strong> ${marcaNome} / ${modeloNome}</div>
                <div class="card-detail"><strong>Ano:</strong> ${v.ano}</div>
                <div class="card-detail"><strong>Cor:</strong> ${v.cor}</div>
                <div class="card-detail"><strong>Preço:</strong> R$ ${v.preco.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}</div>

                ${v.quilometragem ? `<div class="card-detail"><strong>KM:</strong> ${v.quilometragem.toLocaleString("pt-BR")} km</div>` : ""}

                <div class="card-detail"><strong>Cadastrado:</strong> ${new Date(v.dataCadastro).toLocaleDateString("pt-BR")}</div>
            </div>

            <div class="card-actions">
                <button class="btn btn-primary" onclick="editarVeiculo(${v.id})"><i class="fas fa-edit"></i> Editar</button>
                <button class="btn btn-danger" onclick="excluirVeiculo(${v.id})"><i class="fas fa-trash"></i> Excluir</button>
            </div>
        </div>
    `}).join('');
}

function exibirMarcas(marcas) {
    const container = document.getElementById('listaMarcas');

    if (!marcas.length) {
        container.innerHTML = `<div class="loading">Nenhuma marca cadastrada</div>`;
        return;
    }

    // Nota: O backend não está enviando a contagem de modelos,
    // seria preciso lógica extra para buscar isso se for necessário.
    container.innerHTML = marcas.map(m => `
        <div class="card">
            <div class="card-header"><div class="card-title">${m.nome}</div></div>

            <div class="card-details">
                <div class="card-detail"><strong>ID:</strong> ${m.id}</div>
            </div>

            <div class="card-actions">
                <button class="btn btn-danger" onclick="excluirMarca(${m.id})"><i class="fas fa-trash"></i> Excluir</button>
            </div>
        </div>
    `).join('');
}

function exibirModelos(modelos) {
    const container = document.getElementById('listaModelos');

    if (!modelos.length) {
        container.innerHTML = `<div class="loading">Nenhum modelo cadastrado</div>`;
        return;
    }

    container.innerHTML = modelos.map(m => `
        <div class="card">
            <div class="card-header"><div class="card-title">${m.nome}</div></div>

            <div class="card-details">
                <div class="card-detail"><strong>ID:</strong> ${m.id}</div>
                <div class="card-detail"><strong>Marca:</strong> ${m.marca?.nome || "N/A"}</div>
            </div>

            <div class="card-actions">
                <button class="btn btn-primary" onclick="editarModelo(${m.id})"><i class="fas fa-edit"></i> Editar</button>
                <button class="btn btn-danger" onclick="excluirModelo(${m.id})"><i class="fas fa-trash"></i> Excluir</button>
            </div>
        </div>
    `).join('');
}

// ================= FILTROS ==================
function filtrarVeiculos() {
    const texto = document.getElementById('filtroGeral').value.toLowerCase();
    const status = document.getElementById('filtroStatus').value;

    const filtrados = state.veiculos.filter(v => {

        // --- CORREÇÃO AQUI ---
        // A marca agora é acessada através do modelo
        const marca = (v.modelo?.marca?.nome || "").toLowerCase();
        const modelo = (v.modelo?.nome || "").toLowerCase();
        const cor = (v.cor || "").toLowerCase();

        const matchTexto =
            !texto ||
            marca.includes(texto) ||
            modelo.includes(texto) ||
            cor.includes(texto);

        const matchStatus = !status || v.status.toLowerCase() === status.toLowerCase();

        return matchTexto && matchStatus;
    });

    exibirVeiculos(filtrados);
}

function limparFiltros() {
    document.getElementById('filtroGeral').value = "";
    document.getElementById('filtroStatus').value = "";
    exibirVeiculos(state.veiculos);
}

// ================= MODAL VEÍCULO ==================
function carregarMarcasNoSelectVeiculo() {
    const select = document.getElementById('marcaVeiculo');

    select.innerHTML = `<option value="">Selecione a marca</option>`;

    state.marcas.forEach(m => {
        select.innerHTML += `<option value="${m.id}">${m.nome}</option>`;
    });
}

async function carregarModelosPorMarca() {
    const marcaId = document.getElementById('marcaVeiculo').value;
    const select = document.getElementById('modeloVeiculo');

    if (!marcaId) {
        select.innerHTML = `<option>Selecione uma marca</option>`;
        select.disabled = true;
        return;
    }

    select.disabled = false;
    select.innerHTML = `<option>Carregando...</option>`;

    try {
        const response = await fetch(`${API_BASE_URL}/modelos/marca/${marcaId}`);
        const modelos = await response.json();

        select.innerHTML = `<option value="">Selecione o modelo</option>`;
        modelos.forEach(m => {
            select.innerHTML += `<option value="${m.id}">${m.nome}</option>`;
        });
    } catch (err) {
        console.error("Erro ao carregar modelos da marca:", err);
        select.innerHTML = `<option>Erro ao carregar</option>`;
    }
}

function abrirModalVeiculo(v = null) {
    const modal = document.getElementById("modalVeiculo");
    const titulo = document.getElementById("tituloModalVeiculo");
    const form = document.getElementById("formVeiculo");

    form.reset();
    document.getElementById('veiculoId').value = ""; // Limpa o ID
    carregarMarcasNoSelectVeiculo();

    // Reseta e desabilita o select de modelo
    const selectModelo = document.getElementById('modeloVeiculo');
    selectModelo.innerHTML = `<option>Selecione uma marca</option>`;
    selectModelo.disabled = true;

    if (v) {
        titulo.textContent = "Editar Veículo";
        document.getElementById('veiculoId').value = v.id;

        // --- CORREÇÃO AQUI ---
        // A marca agora é acessada através do modelo
        document.getElementById('marcaVeiculo').value = v.modelo?.marca?.id || "";

        // Carrega os modelos daquela marca e DEPOIS seleciona o modelo do veículo
        carregarModelosPorMarca().then(() => {
            document.getElementById('modeloVeiculo').value = v.modelo?.id || "";
        });

        document.getElementById('ano').value = v.ano;
        document.getElementById('cor').value = v.cor;
        document.getElementById('preco').value = v.preco;
        document.getElementById('quilometragem').value = v.quilometragem || "";
        document.getElementById('status').value = v.status;

    } else {
        titulo.textContent = "Cadastrar Veículo";
    }

    modal.style.display = "block";
}

function fecharModalVeiculo() {
    document.getElementById("modalVeiculo").style.display = "none";
}

async function salvarVeiculo(event) {
    event.preventDefault();

    const id = document.getElementById("veiculoId").value;

    // --- CORREÇÃO AQUI ---
    // O VeiculoDTO/Veiculo no backend não espera mais a "marca",
    // apenas o "modelo". O backend vai descobrir a marca através do modelo.
    const veiculo = {
        modelo: { id: parseInt(document.getElementById('modeloVeiculo').value) },
        ano: parseInt(document.getElementById('ano').value),
        cor: document.getElementById('cor').value,
        preco: parseFloat(document.getElementById('preco').value),
        quilometragem: document.getElementById('quilometragem').value ? parseFloat(document.getElementById('quilometragem').value) : null,
        status: document.getElementById('status').value
    };

    let url = `${API_BASE_URL}/veiculos`;
    let method = "POST";

    if (id) {
        url = `${API_BASE_URL}/veiculos/${id}`;
        method = "PUT";
    }

    try {
        const response = await fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(veiculo)
        });

        if (!response.ok) {
            const errText = await response.text();
            throw new Error(errText || "Erro ao salvar veículo");
        }

        fecharModalVeiculo();
        await carregarVeiculos(); // Recarrega a lista de veículos
        await carregarModelos(); // Recarrega os modelos (pode ter mudado contagem de veículos)
        showToast("Veículo salvo com sucesso!", "success");

    } catch (error) {
        showToast(error.message, "error");
        console.error("Erro ao salvar veículo:", error);
    }
}

function editarVeiculo(id) {
    const v = state.veiculos.find(x => x.id === id);
    if (v) abrirModalVeiculo(v);
}

async function excluirVeiculo(id) {
    if (!confirm("Deseja realmente excluir este veículo?")) return;

    try {
        const response = await fetch(`${API_BASE_URL}/veiculos/${id}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            const errText = await response.text();
            throw new Error(errText || "Erro ao excluir");
        }

        await carregarVeiculos();
        await carregarModelos(); // Recarrega os modelos (pode ter mudado contagem)
        showToast("Veículo excluído!", "success");
    } catch (error) {
        showToast(error.message, "error");
        console.error("Erro ao excluir veículo:", error);
    }
}

// ================= MARCAS ==================
function abrirModalMarca() {
    document.getElementById('formMarca').reset();
    document.getElementById('marcaId').value = ""; // Garante que é um cadastro
    document.getElementById('tituloModalMarca').textContent = "Cadastrar Marca";
    document.getElementById('modalMarca').style.display = "block";
}

function fecharModalMarca() {
    document.getElementById('modalMarca').style.display = "none";
}

async function salvarMarca(event) {
    event.preventDefault();

    const nome = document.getElementById('nomeMarca').value;
    // O ID da marca não é usado para salvar, mas poderia ser para editar
    // const id = document.getElementById('marcaId').value;

    try {
        const response = await fetch(`${API_BASE_URL}/marcas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome })
        });

        if (!response.ok) {
            const errText = await response.text();
            throw new Error(errText || "Erro ao salvar marca");
        }

        fecharModalMarca();
        await carregarMarcas(); // Recarrega lista de marcas
        showToast("Marca salva!", "success");
    } catch (error) {
        showToast(error.message, "error");
        console.error("Erro ao salvar marca:", error);
    }
}

async function excluirMarca(id) {
    if (!confirm("Excluir esta marca? Modelos associados também podem ser afetados.")) return;

    try {
        const response = await fetch(`${API_BASE_URL}/marcas/${id}`, { method: "DELETE" });

        if (!response.ok) {
            const errText = await response.text(); // Pega a msg do backend (ex: "Não pode excluir")
            throw new Error(errText || "Erro ao excluir marca");
        }

        await carregarMarcas(); // Recarrega marcas
        await carregarModelos(); // Recarrega modelos (podem ter sido afetados)
        await carregarVeiculos(); // Recarrega veículos (podem ter sido afetados)
        showToast("Marca excluída", "success");
    } catch (error) {
        showToast(error.message, "error");
        console.error("Erro ao excluir marca:", error);
    }
}

// ================= MODELOS ==================

// (Esta função já existia e está correta)
function abrirModalModelo(modelo = null) {
    const modal = document.getElementById("modalModelo");
    const titulo = document.getElementById("tituloModalModelo");
    const form = document.getElementById("formModelo");

    form.reset();
    document.getElementById("modeloId").value = ""; // Limpa ID
    carregarMarcasNoSelectModelo(); // <- Isso agora funciona

    if (modelo) {
        titulo.textContent = "Editar Modelo";
        document.getElementById("modeloId").value = modelo.id;
        document.getElementById("nomeModelo").value = modelo.nome;
        document.getElementById("marcaModelo").value = modelo.marca?.id || "";
    } else {
        titulo.textContent = "Cadastrar Modelo";
    }

    modal.style.display = "block";
}

// (Esta função já existia e está correta)
function editarModelo(id) {
    const modelo = state.modelos.find(m => m.id === id);
    if (modelo) abrirModalModelo(modelo);
}

// --- FUNÇÕES QUE FALTAVAM ---

/**
 * (FUNÇÃO ADICIONADA)
 * Fecha o modal de cadastro/edição de modelos.
 */
function fecharModalModelo() {
    document.getElementById("modalModelo").style.display = "none";
}

/**
 * (FUNÇÃO ADICIONADA)
 * Carrega a lista de marcas no <select> do modal de modelos.
 */
function carregarMarcasNoSelectModelo() {
    const select = document.getElementById('marcaModelo');
    select.innerHTML = `<option value="">Selecione a marca</option>`;

    // Reutiliza o state de marcas já carregado
    state.marcas.forEach(m => {
        select.innerHTML += `<option value="${m.id}">${m.nome}</option>`;
    });
}

/**
 * (FUNÇÃO ADICIONADA)
 * Salva um novo modelo ou atualiza um existente.
 */
async function salvarModelo(event) {
    event.preventDefault();

    const id = document.getElementById("modeloId").value;
    const nome = document.getElementById("nomeModelo").value;
    const marcaId = document.getElementById("marcaModelo").value;

    if (!marcaId || !nome) {
        showToast("Nome e Marca são obrigatórios.", "error");
        return;
    }

    // Monta o objeto no formato que o backend espera (com o objeto 'marca' aninhado)
    const modelo = {
        nome: nome,
        marca: { id: parseInt(marcaId) }
    };

    let url = `${API_BASE_URL}/modelos`;
    let method = "POST";

    if (id) {
        url = `${API_BASE_URL}/modelos/${id}`;
        method = "PUT";
        modelo.id = parseInt(id); // Inclui o ID para atualização
    }

    try {
        const response = await fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(modelo)
        });

        if (!response.ok) {
            const errText = await response.text();
            throw new Error(errText || "Erro ao salvar modelo");
        }

        fecharModalModelo();
        await carregarModelos(); // Recarrega a lista de modelos na tela
        await carregarVeiculos(); // Recarrega veículos (nomes podem ter mudado)
        showToast("Modelo salvo com sucesso!", "success");

    } catch (error) {
        showToast(error.message, "error");
        console.error("Erro ao salvar modelo:", error);
    }
}

/**
 * (FUNÇÃO ADICIONADA)
 * Exclui um modelo pelo ID.
 */
async function excluirModelo(id) {
    if (!confirm("Deseja realmente excluir este modelo?")) return;

    try {
        const response = await fetch(`${API_BASE_URL}/modelos/${id}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            // Tenta ler a mensagem de erro do seu backend (Ex: "Não pode excluir")
            const errText = await response.text();
            throw new Error(errText || "Erro ao excluir modelo");
        }

        await carregarModelos(); // Recarrega a lista
        await carregarVeiculos(); // Veículos podem ter sido afetados
        showToast("Modelo excluído com sucesso!", "success");

    } catch (error) {
        showToast(error.message, "error");
        console.error("Erro ao excluir modelo:", error);
    }
}


// ================= RELATÓRIOS ==================
async function carregarRelatorios() {
    carregarRelatorioStatus();
    carregarRelatorioValor();
}

async function carregarRelatorioStatus() {
    try {
        const response = await fetch(`${API_BASE_URL}/veiculos/relatorio/status`);
        const data = await response.json(); // Assumindo que o backend retorna JSON

        // Formata o JSON para exibição
        let html = "<ul>";
        for (const [key, value] of Object.entries(data)) {
            html += `<li><strong>${key}:</strong> ${value}</li>`;
        }
        html += "</ul>";
        document.getElementById("relatorioStatus").innerHTML = html;

    } catch (err) {
        console.error("Erro Relatório Status:", err);
        document.getElementById("relatorioStatus").innerHTML = "<p style='color:red'>Erro ao carregar relatório de status.</p>";
    }
}

async function carregarRelatorioValor() {
    try {
        const response = await fetch(`${API_BASE_URL}/veiculos/relatorio/valor-estoque`);

        // 1. Pega o objeto JSON (ex: {"valorTotal": 120000.00})
        const data = await response.json();

        // 2. Acessa o NÚMERO *dentro* do objeto.
        //    (Estou assumindo que o nome da propriedade é "valorTotal".
        //     Se não funcionar, pode ser "valor" ou "total".
        //     Confira na aba "Rede" do F12 qual é o nome certo.)
        const valorNumerico = data.valorTotal;

        // 3. Formata o NÚMERO, e não o objeto
        const valorFormatado = valorNumerico.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });

        document.getElementById("relatorioValor").innerHTML =
            `<h3>Valor Total em Estoque: ${valorFormatado}</h3>`;

    } catch (err) {
        console.error("Erro Relatório Valor:", err);
        document.getElementById("relatorioValor").innerHTML = "<p style='color:red'>Erro ao carregar relatório de valor.</p>";
    }
}
// ================= UTILITÁRIOS ==================
function showLoading(id) {
    document.getElementById(id).innerHTML = `<div class="loading">Carregando...</div>`;
}

function showError(id, msg) {
    document.getElementById(id).innerHTML = `<div class="loading" style="color:red">${msg}</div>`;
}

function showToast(msg, type = "success") {
    const toast = document.getElementById("toast");
    toast.textContent = msg;
    toast.className = `toast show ${type}`;
    setTimeout(() => toast.classList.remove("show"), 3000);
}


// ================= EXPORT GLOBAL ==================
// Expõe as funções para serem usadas pelos 'onclick' no HTML

window.abrirModalVeiculo = abrirModalVeiculo;
window.fecharModalVeiculo = fecharModalVeiculo;
window.salvarVeiculo = salvarVeiculo;
window.editarVeiculo = editarVeiculo;
window.excluirVeiculo = excluirVeiculo;
window.carregarModelosPorMarca = carregarModelosPorMarca; // Importante para o onchange do select

window.abrirModalMarca = abrirModalMarca;
window.fecharModalMarca = fecharModalMarca;
window.salvarMarca = salvarMarca;
window.excluirMarca = excluirMarca;

window.abrirModalModelo = abrirModalModelo;
window.fecharModalModelo = fecharModalModelo; // <- CORRIGIDO
window.salvarModelo = salvarModelo;           // <- CORRIGIDO
window.editarModelo = editarModelo;
window.excluirModelo = excluirModelo;         // <- CORRIGIDO

window.filtrarVeiculos = filtrarVeiculos;
window.limparFiltros = limparFiltros;
window.carregarRelatorioStatus = carregarRelatorioStatus;
window.carregarRelatorioValor = carregarRelatorioValor;