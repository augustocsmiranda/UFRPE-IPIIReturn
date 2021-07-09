package gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import controllers.ControllerProcesso;
import excecoes.NaoHaInformacaoDisponivelException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Atendente;
import model.Cliente;
import model.Funcionario;
import model.Processo;

public class TelaConsultaProcessosController {

	@FXML
	private MenuItem menuRelatorio;
	@FXML
	private MenuItem menuAtendimento;
	@FXML
	private MenuItem menuMonitor;
	@FXML
	private MenuItem menuPainelAdm;
	@FXML
	private MenuItem menuProcessos;
	@FXML
	private MenuItem menuManutencao;
	@FXML
	private MenuItem menuGuiche;
	@FXML
	private MenuItem menuLogout;
	@FXML
	private MenuItem exibirDescricao;
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtCliente;
	@FXML
	private DatePicker dpPrimeiraData;
	@FXML
	private DatePicker dpSegundaData;
	@FXML
	private Button dadosASeremExibidos;
	
	@FXML private TableView<Processo> tableProcessos = new TableView<Processo>();
    @FXML private TableColumn<Processo, String> columnId;
    @FXML private TableColumn<Processo, String> columnCliente;
    @FXML private TableColumn<Processo, String> columnTipo;
    @FXML private TableColumn<Processo, LocalDate> columnDataAbertura;
    @FXML private TableColumn<Processo, String> columnStatus;
    
    public ControllerProcesso controllerProcesso = new ControllerProcesso();

    @FXML
    public void initialize() throws Exception {
    	columnId.setCellValueFactory(new PropertyValueFactory<>("numero"));
    	columnCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
    	columnTipo.setCellValueFactory(new PropertyValueFactory<>("descricao"));  
    	columnDataAbertura.setCellValueFactory(new PropertyValueFactory<>("dataAbertura"));  
    	columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));  
    	columnId.setPrefWidth(60);
        columnCliente.setPrefWidth(140);
        columnTipo.setPrefWidth(81);
        columnDataAbertura.setPrefWidth(120);
        columnStatus.setPrefWidth(110);
        Cliente cliente = new Cliente();
        cliente.setNome("Fulano");
        Atendente atendente = new Atendente();
        atendente.setNome("Sicrano");
        
        
        Processo processo = new Processo("2", LocalDate.now(), "Fulano", "Algum processo", "Tramitando", atendente);
        Processo processo1 = new Processo("5", LocalDate.now(), "Sicrano", "processo2", "Indeferido", atendente);

        controllerProcesso.salvar(processo);
        controllerProcesso.salvar(processo1);
        
        atualizarConsultaProcessos(controllerProcesso.listar());
    }
	
    public void atualizarConsultaProcessos(List<Processo> lista)  throws Exception{
		ObservableList<Processo> listaProcessos = FXCollections.observableArrayList();
		listaProcessos.addAll(lista); 
		tableProcessos.setItems(listaProcessos);
//		if(listaProcessos.isEmpty() || listaProcessos == null) {
//			throw new NaoHaInformacaoDisponivelException(); 
//		}
		System.out.println(tableProcessos.toString());
	}
	
   
   public void dadosASeremExibidosPorData() throws Exception {
	   
	   LocalDate dataInicial = LocalDate.of(this.dpPrimeiraData.getValue().getYear(), this.dpPrimeiraData.getValue().getMonthValue(), this.dpPrimeiraData.getValue().getDayOfMonth());
	   LocalDate dataFinal = LocalDate.of(this.dpSegundaData.getValue().getYear(), this.dpSegundaData.getValue().getMonthValue(), this.dpSegundaData.getValue().getDayOfMonth());

	   List<Processo> listaDeProcessosData = controllerProcesso.listar();
	   List<Processo> processosDentroDoPeriodo = new ArrayList<Processo>();
	   
	   for (Processo processo:listaDeProcessosData) {
		   
		   if(processo.getDataAbertura().isBefore(dataFinal) && processo.getDataAbertura().isAfter(dataInicial)) {
			   processosDentroDoPeriodo.add(processo);
		   }
	   }
	   
	   this.atualizarConsultaProcessos(processosDentroDoPeriodo);
   }
   
   public void dadosASeremExibidosPorCliente() throws Exception {
	   List<Processo> listaProcessosCliente = controllerProcesso.listar();
	   List<Processo> processosDoCliente = new ArrayList<Processo>();
	   
	   for(Processo processo : listaProcessosCliente) {
		   
		   if(processo.getCliente().toLowerCase().equals(this.txtCliente.getText().toLowerCase())) {
			   processosDoCliente.add(processo);
		   }
	   }
	   this.atualizarConsultaProcessos(processosDoCliente); 
   }
   
   public void dadosASeremExibidosPorId() throws Exception {
	   List<Processo> listaProcessosId = controllerProcesso.listar();
	   List<Processo> processoIdCorrespondente = new ArrayList<Processo>();
	   
	   for(Processo processo:listaProcessosId) {
		   if(processo.getNumero().equals(txtId.getText())) {
			   processoIdCorrespondente.add(processo);
			   break;
		   }
	   }
	   
	   this.atualizarConsultaProcessos(processoIdCorrespondente);
	    
   }
	

	@FXML
	public void consultar() throws Exception {
		if(txtId.toString() != null || txtId.toString() != "") {
			dadosASeremExibidosPorId();
		}
		else if (this.txtCliente.toString() != null && this.txtCliente.toString() != "") {
			System.out.println("Entrei aqui");
			dadosASeremExibidosPorCliente();
		}
		else if(this.dpPrimeiraData != null && this.dpSegundaData != null) {
			dadosASeremExibidosPorData();
		}
	}
   
	@FXML
	public void mudarRelatorios() {
		Main.mudarTela("relatorioA");
	}
	@FXML
	public void mudarMonitor() {
		Main.mudarTela("monitor");
	}
	@FXML
	public void mudarPainelAdm() {
		Main.mudarTela("painelAdm");
	}
	@FXML
	public void mudarGuiche() {
		Main.mudarTela("gerenciamentoG");
	}
	@FXML
	public void mudarManutencao() {
		Main.mudarTela("manutencao");
	}
	@FXML
	public void mudarAtendimento() {
		Main.mudarTela("atendimento");
	}
	@FXML
	public void mudarProcessos() {
		Main.mudarTela("processo");
	}
	@FXML
	public void mudarLogin() {
		Main.mudarTela("login");
	}
	@FXML
	public void exibirDescricao() {
		Alert popup = new Alert(AlertType.INFORMATION);
		popup.setHeaderText("");
		popup.setTitle("Descri��o da tela");
		popup.setContentText("Nesta tela ser� poss�vel realizar a consulta de processos em aberto usando filtro por per�odo, cliente ou id do processo..");
		popup.show();
	}
	
}
