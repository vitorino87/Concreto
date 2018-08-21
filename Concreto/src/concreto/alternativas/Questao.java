package concreto.alternativas;


import concreto.alternativas.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.graphics.Color;

///////////////////////////////////////// INÍCIO DA CLASSE ///////////////////////////////////////////////////////////////////////////////////////////////////	

public class Questao extends QuestaoConector implements OnItemSelectedListener, OnTouchListener, OnGestureListener, OnDoubleTapListener{
	
///////////////////////////////// VARIÁVEIS PÚBLICAS /////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	
	// variável utilizada para trabalhar com o método checked
	private int z = -1;	

	// variável para trabalhar com o método embaralhar
	private int[] w = new int[h];

	// Variáveis para tratar com os objetos da tela2
	TextView tv;
	RadioButton[] rd = new RadioButton[5];
	Button btnRandom, btnChecar, btnBuscar, btnAnterior, btnProximo, btnSortear, btnTema;
	EditText txtBuscar;
	Spinner btnSpinner;

	// variáveis para impedir que o random repita números
	private int f = 0;
	
	// variável global para auxiliar contagem das questões temáticas
	private int auxiliarTema = 0;
	
	//Essas variáveis são utilizadas para trabalhar com o Spinner
	List <String> temas;
	List <Integer> conectorAuxiliar;	
	List <Integer> questoesTematicas;
	LinearLayout linearLayout3, linearLayout2, linearLayout1;

	//variável auxiliar para poder embaralhar as alternativas
	private List <Integer> auxiliarEmbaralharAlternativas = null;	
	
	//variável de recursos
	Resources rc;
	
	//private static final String TAG = "Gestures";
	GestureDetector gestureDetector;
	
	ScrollView sv;
	
	
///////////////////////////////// FIM DAS VARIÁVEIS PÚBLICAS /////////////////////////////////////////////////////////////////////////////////////////////////////////	

	
	
///////////////////////////////////////// INÍCIO DO MÉTODO PRINCIPAL ////////////////////////////////////////////////////////////////////////////////////////////////
	
	// método para trabalhar com a tela2
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		z = restaurarEstado();
		w = restaurarW();
		setContentView(R.layout.tela2);
		tv = (TextView) findViewById(R.id.textView1); // ligando os objetos das
														// telas às variáveis
		rd[0] = (RadioButton) findViewById(R.id.radio0);
		rd[1] = (RadioButton) findViewById(R.id.radio1);
		rd[2] = (RadioButton) findViewById(R.id.radio2);
		rd[3] = (RadioButton) findViewById(R.id.radio3);
		rd[4] = (RadioButton) findViewById(R.id.radio4);
		btnRandom = (Button) findViewById(R.id.btnRandomizar);
		btnChecar = (Button) findViewById(R.id.btnChecar);
		btnBuscar = (Button) findViewById(R.id.btnBuscar);
		btnAnterior = (Button)findViewById(R.id.btnAnterior);
		btnProximo = (Button)findViewById(R.id.btnProximo);
		btnSortear = (Button)findViewById(R.id.btnSortear);
		txtBuscar = (EditText) findViewById(R.id.editText1);
		btnSpinner = (Spinner) findViewById(R.id.escolhedor);
		btnTema = (Button) findViewById(R.id.btnTema);
		questoesTematicas = new ArrayList<Integer>();		
		sv = (ScrollView) findViewById(R.id.scrollView);
		
		if(z!=-1){
			carregarQuestao(z);
			f=z;
		}
		
		//chamando o método que muda as cores
		mudarACorDosLayouts();
				
		//CustomGestureDetector customGestureDetector = new CustomGestureDetector();	
		gestureDetector = new GestureDetector(Questao.this, Questao.this);
		
		//MainActivity ma = new MainActivity(Questao.this);				

/////////////////////////////INÍCIO DOS MÉTODOS DOS BOTÕES////////////////////////////////////////////////////////////////						
		sv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				gestureDetector.onTouchEvent(event);
				sv.onTouchEvent(event);
				return true;
			}
		});
		
		for(int i=0;i<5;i++){
			rd[i].setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					gestureDetector.onTouchEvent(event);
					return false;
				}
			});
		}						
		
		// método para trabalhar com o Sortear
		btnSortear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (f < h) {
					// o array w possui os números das questões embaralhadas, a
					// variável f irá orientar esse array, iniciando de 0 até ...
					z = w[f];
					carregarQuestao(z);
					f++; // incrementando a variável f que armazena a posição do
							// array que está sendo percorrido.
					mudarACorDosLayouts();
				}
			}			
		});

		// método para trabalhar com o botão Checar
		btnChecar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				QuestaoAux.checked(z, b, rd, Questao.this, auxiliarEmbaralharAlternativas);
			}
		});

		// método para trabalhar com o botão Buscar
		btnBuscar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				carregarQuestao(Integer.parseInt(txtBuscar.getText().toString()));
				mudarACorDosLayouts();
			}
		});
		
		//método para trabalhar com o botão Anterior
		btnAnterior.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (z > 0) {
					carregarQuestao(--z);
				}
				mudarACorDosLayouts();
			}
		});
		
		//método para trabalhar com o botão Próximo
		btnProximo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int aux = h - 1;
				if (z < aux) {
					carregarQuestao(++z);
				}
				mudarACorDosLayouts();
			}
		});
		
		//método para trabalhar com o botão Random
		//Tudo que esse método faz é randomizar, apenas isso... 
		//Após clicar em Randomizar, é necessário clicar em Sortear para que
		//esse método faça sentido... Caso contrário, se o usuário ficar sempre clicando no
		//botão Randomizar, o array na posição w[0] sempre será atualizado, e não será reaproveitado as
		//outras posições
		btnRandom.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				z=f=0;
				QuestaoAux.embaralhar(w);
				carregarQuestao(w[f]);
				mudarACorDosLayouts();
			}
		});
		
		//método para selecionar perguntas do mesmo tema, ele trabalha em conjunto com 
		// O CÓDIGO PARA TRABALHAR COM O SPINNER e com os
		// MÉTODOS PARA TRABALHAR COM O ITEM SELECIONADO PELO SPINNER
		btnTema.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				carregarQuestao(questoesTematicas.get(auxiliarTema++));
				mudarACorDosLayouts();
			}
		});
		
/////////////////////////////FIM DOS MÉTODOS DOS BOTÕES////////////////////////////////////////////////////////////////		
				
		rc = this.getResources();
		
		temas = new ArrayList<String>();
		temas.add("TODOS OS TEMAS"); //essa adição serve apenas para deixar o primeiro item vazio
		conectorAuxiliar = new ArrayList<Integer>();
		//enderecoDaQuestao = new ArrayList<Integer>();
				
		for(int i=0;i<get_QtdeDeQuestoes();i++){                      //esse laço permite percorrer por todas as questões
			String a = rc.getString(get_EnderecoDaQuestao(i));        //essa linha faz o resgate do enunciado da questão
			String b = (String) a.subSequence(a.indexOf("[")+1, a.indexOf("]"));	//essa linha faz a limpeza do enunciado da questão, retornando apenas o tema			
			if(temas.indexOf(b)==-1){                                    //essa linha verifica se o tema já existe na variável temas
				temas.add(b);												//se o tema não existir, ele será adicionado											
			}															
			conectorAuxiliar.add(temas.indexOf(b));	//essa variável irá armazenar a posição da variável temas correspondente a variável b
													//por que essa posição? Porque ela é única e mais fácil de processar do que uma String.
													//Assim, cada elemento desta lista terá um valor relacionado ao tema correspondente.
													//Num contexto geral, essa variável faz a ligação entre o tema e a questão do tema
		}		
		
///////////////////////INÍCIO DO CÓDIGO PARA TRABALHAR COM O SPINNER/////////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		// O código abaixo foi retirado na internet especificamente do Tutorials Point
		// Segue o endereço https://www.tutorialspoint.com/android/android_spinner_control.htm
		// Alterei apenas a variável categorias por temas
		// Spinner click listener
	      btnSpinner.setOnItemSelectedListener((OnItemSelectedListener) this);
	      
	      // Creating adapter for spinner
	      ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, temas);
	      
	      // Drop down layout style - list view with radio button
	      dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	      
	      // attaching data adapter to spinner
	      btnSpinner.setAdapter(dataAdapter);			      
	      
///////////////////////FIM DO CÓDIGO PARA TRABALHAR COM O SPINNER/////////////////////////////////////////////////////////////////////////////////////////////////////////		      
	}
	
///////////////////////////////////////// FIM DO MÉTODO PRINCIPAL ////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
///////////////////////////////////////// INÍCIO DOS MÉTODOS AUXILIARES //////////////////////////////////////////////////////////////////////////////////////////////////	
		
	
	
	@SuppressLint("DefaultLocale")
	public void mudarACorDosLayouts(){
		linearLayout3 = (LinearLayout)findViewById(R.id.linearlayout3);
		linearLayout2 = (LinearLayout)findViewById(R.id.linearLayout2);
		linearLayout1 = (LinearLayout)findViewById(R.id.linearLayout1);
		Random random = new Random();
		String id = String.format("%06d", random.nextInt(999999));
		linearLayout3.setBackgroundColor(Color.parseColor("#"+id));
		id = String.format("%06d", random.nextInt(999999));
		linearLayout2.setBackgroundColor(Color.parseColor("#"+id));
		id = String.format("%06d", random.nextInt(999999));
		linearLayout1.setBackgroundColor(Color.parseColor("#"+id));
		//Duas formas de mudar de cor
		//rl.setBackgroundColor(Color.parseColor("#01ff90"));
		//rl.setBackgroundColor(Color.parseColor("blue"));
	}
	
	
//////// MÉTODO PARA TRABALHAR COM O ITEM SELECIONADO PELO SPINNER/////////////////////////////////////////////
	@Override
	   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	      // On selecting a spinner item
	      String item = parent.getItemAtPosition(position).toString();
	      if(item!="TODOS OS TEMAS"){
	    	  carregarArrayComQuestoesDoTemaSelecionado(item);	
	    	  carregarQuestao(questoesTematicas.get(auxiliarTema++));
	      }
	      // Showing selected spinner item
	      Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();	      
	      
	   }
	   public void onNothingSelected(AdapterView<?> arg0) {
	      // TODO Auto-generated method stub
	   }
	   
	   public void carregarArrayComQuestoesDoTemaSelecionado(String tema){
		   int posicaoDoTema = temas.indexOf(tema);
		   int i = conectorAuxiliar.size();
		   for(int j=0;j<i;j++){
			   if(conectorAuxiliar.get(j) == posicaoDoTema){
				   questoesTematicas.add(j);
			   }
		   }
	   }
	   
////////FIM DO MÉTODO PARA TRABALHAR COM O ITEM SELECIONADO PELO SPINNER///////////////////////////////////////	   	  
	   
	   	   
	/**
	 * Serve para carregar uma questão na tela2
	 * @param questao
	 * precisa informar o número da questão por meio da variável questao
	 */
	public void carregarQuestao(int questao) {
		z = questao;										//* a variável z armazena o número da questão para verificar se ela está correta		 		 		
		tv.setText(a[questao]);								//* a variável tv carrega o enunciado na tela
		List<Integer>numbers = new ArrayList<Integer>();		
		for(int i=0;i<5;i++){
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		String[] abcde = {"a) ","b) ","c) ","d) ","e) "};		
		for(int i=0;i<5;i++){			
			 rd[i].setText(b[questao][numbers.get(i)]);						//* o array rd carrega cada uma das alternativas
			 rd[i].setText(abcde[i] + rd[i].getText());
		}
		
		txtBuscar.setText(String.valueOf(questao));			//* a variável txtBuscar carrega o número da questão na tela
		
		auxiliarEmbaralharAlternativas = numbers;
	}
	/*
	 * Serve para guardar as variáveis atuais do app
	 * Permitindo que o app continue de onde parou
	 */
	public void guardarEstado(){
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("z", z);
		for(int i=0;i<h;i++){
			String cat="aux"+i;
			editor.putInt(cat, w[i]);
		}
		editor.commit();
	}
	
	/*
	 * Serve para restaurar uma variável guardada pelo método guardarEstado()
	 *  A variável que ele restaura é da última questão que foi apresentada na tela
	 */
	public int restaurarEstado(){
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		int defaultValue = -1;
		int value = sharedPref.getInt("z", defaultValue);
		return value;
	}
	
	/*
	 * Serve para restaurar o array guardado pelo método guardarEstado()
	 * O array que ele restaura é o utilizado para fazer o random
	 */
	public int[] restaurarW(){
		int[] aux=new int[h];
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		for(int i=0;i<h;i++){
			int defaultValue = i;
			aux[i]=sharedPref.getInt("aux"+i, defaultValue);
		}
		return aux;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		guardarEstado();
	}
	@Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }
	@Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }
	@Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }
///////////////////////////////////////// FIM DOS MÉTODOS AUXILIARES //////////////////////////////////////////////////////////////////////////////////////////////////		

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float velocityX, float velocityY) {
		if (motionEvent1.getY() - motionEvent2.getY() > 50) {
            //Toast.makeText(this, "You Swiped up!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (motionEvent2.getY() - motionEvent1.getY() > 50) {
            //Toast.makeText(this, "You Swiped Down!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (motionEvent1.getX() - motionEvent2.getX() > 50) {
            //Toast.makeText(this, "You Swiped Left!", Toast.LENGTH_LONG).show();
            int aux = h - 1;
			if (z < aux) {
				carregarQuestao(++z);
			}
			mudarACorDosLayouts();
            return true;
        }

        if (motionEvent2.getX() - motionEvent1.getX() > 50) {
            //Toast.makeText(this, "You Swiped Right!", Toast.LENGTH_LONG).show();
            if (z > 0) {
				carregarQuestao(--z);
			}
			mudarACorDosLayouts();
            return true;
        } else {
            return true;
        }
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		if(z!=-1){
			QuestaoAux.checked(z, b, rd, Questao.this, auxiliarEmbaralharAlternativas);
		}		
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}

///////////////////////////////////////// FIM DA CLASSE //////////////////////////////////////////////////////////////////////////////////////////////////	