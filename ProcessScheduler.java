import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import java.awt.*;
import javax.swing.ScrollPaneConstants;

public class ProcessScheduler extends JFrame { // Process Scheduler GUI

	private JPanel contentPane; // 프레임을 담기 위한 패널
	private JTextField Name_text; // 글자 출력을 위한 텍스트필드
	private JTextField AT_text;
	private JTextField BT_text;
	private JTextField TimeQuantum_text;
	private JTable JobPool; // 값 출력을 위한 테이블
	private JTable gant_table; // 간트차트 출력을 위한 테이블
	private JTable PN_table; // 간트차트에 프로세스 이름을 출력하기 위한 테이블

	public static Object[][] rowData = null; // Job Pool 테이블 인덱스
	public static String coulumnNames[] = { "Name", "AT", "WT", "BT", "TT", "NTT" };
	public static Object[][] gant_x = null; // 간트차트 테이블 인덱스
	public static String gant_y[] = null;
	public static Object[][] PN_x = null; // 간트차트 프로세스 이름 테이블 인덱스
	public static String PN_y[] = null;
	public static String[] nameArr = null; // 프로세스 이름을 받는 배열
	public static ArrayList arrival = new ArrayList(); // 프로세스 도착시간들을 받는 배열리스트
	public static ArrayList burst = new ArrayList(); // 프로세스 수행시간들을 받는 배열리스트
	public static double[] waitArr = null; // 프로세스 대기시간들을 받는 배열
	public static double[] turnArr = null; // 프로세스 반환시간들을 받는 배열
	public static double[] NTTArr = null; // 프로세스의 정규화 값을 받는 배열
	public static int[] endArr = null; // 프로세스가 끝나는 시간을 받는 배열
	public static int[] paintArr = null; // 프로세스가 끝나는 순서를 받아 간트차트에서 사용할 배열
	public static int N = 0; // 프로세스 수
	public static int quantum = 0; // quantum 값

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { // java swing을 위한 이벤트 처리
			public void run() {
				try {
					ProcessScheduler frame = new ProcessScheduler(); // gui를 호출함
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ProcessScheduler() {
		setTitle("Process Scheduler"); // 실행창의 이름
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창을 닫으면 메모리를 지움
		setBounds(100, 100, 1190, 503);
		contentPane = new JPanel(); // 패널 선언
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // 자유로운 배치를 위해 EmptyBorder로 만듦
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblAlgorithms = new JLabel("Algorithm :"); // 라벨링
		lblAlgorithms.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblAlgorithms.setBounds(51, 12, 74, 18);
		contentPane.add(lblAlgorithms);

		JLabel lblProcessNum = new JLabel("Process Name :");
		lblProcessNum.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblProcessNum.setBounds(22, 46, 110, 18);
		contentPane.add(lblProcessNum);

		JLabel lblNewLabel = new JLabel("Arrival Time :");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblNewLabel.setBounds(34, 76, 93, 18);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Burst Time :");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(43, 106, 90, 18);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Time Quantum :");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblNewLabel_2.setBounds(21, 136, 105, 18);
		contentPane.add(lblNewLabel_2);

		JLabel lblJobPool = new JLabel("Job Pool");
		lblJobPool.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblJobPool.setBounds(586, 12, 62, 18);
		contentPane.add(lblJobPool);

		JLabel lblGanttChart = new JLabel("Gantt Chart");
		lblGanttChart.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblGanttChart.setBounds(12, 201, 107, 15);
		contentPane.add(lblGanttChart);

		JPanel panel = new JPanel(); // 코멘트를 남길 패널 선언
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(356, 39, 223, 150);
		contentPane.add(panel);

		JLabel lblNewLabel_3 = new JLabel("Developers'_commentary");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblNewLabel_3.setBounds(356, 14, 201, 15);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel(
				"<html>Process_name, BT, AT <br>\uC785\uB825\uC2DC \uB744\uC5B4\uC4F0\uAE30\uB85C \uAD6C\uBD84\uD574 \uC8FC\uC138\uC694 <br>PN,AT,BT\uB294 insert, WT,TT,NTT\uB294<br> start \uBC84\uD2BC\uC744 \uB204\uB974\uBA74 \uCD9C\uB825\uB429\uB2C8\uB2E4.<br>PN &nbsp p1  p2 p3 p4 <br>AT &nbsp 1 &nbsp 2 &nbsp 3 &nbsp 4 <br>BT &nbsp 3 &nbsp 4 &nbsp 2 &nbsp 2");
		lblNewLabel_4.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		panel.add(lblNewLabel_4);

		JComboBox algorithmBox = new JComboBox(); // 알고리즘 선택
		algorithmBox.setBackground(new Color(248, 248, 255));
		algorithmBox.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		algorithmBox.setModel(new DefaultComboBoxModel(new String[] { "FCFS", "RR", "SPN", "SRTN", "HRRN", "RRR" }));
		algorithmBox.setToolTipText("");
		algorithmBox.setBounds(136, 9, 212, 24);
		contentPane.add(algorithmBox);

		Name_text = new JTextField(); // 프로세스 이름
		Name_text.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		Name_text.setBounds(136, 43, 212, 24);
		contentPane.add(Name_text);
		Name_text.setColumns(10);

		AT_text = new JTextField(); // 도착시간
		AT_text.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		AT_text.setBounds(136, 73, 212, 24);
		contentPane.add(AT_text);
		AT_text.setColumns(10);

		BT_text = new JTextField(); // 실행시간
		BT_text.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		BT_text.setBounds(136, 103, 212, 24);
		contentPane.add(BT_text);
		BT_text.setColumns(10);

		TimeQuantum_text = new JTextField(); // Time Quantum
		TimeQuantum_text.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		TimeQuantum_text.setBounds(136, 136, 212, 24);
		contentPane.add(TimeQuantum_text);
		TimeQuantum_text.setColumns(10);

		JButton btnInsert = new JButton("Insert"); // 삽입 버튼
		btnInsert.setBackground(new Color(248, 248, 255));
		btnInsert.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnInsert.setBounds(14, 164, 105, 27);
		contentPane.add(btnInsert);

		JButton btnStart = new JButton("Start"); // 시작 버튼
		btnStart.setBackground(new Color(248, 248, 255));
		btnStart.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnStart.setBounds(129, 164, 105, 27);
		contentPane.add(btnStart);
		btnStart.setEnabled(false);

		JButton btnReset = new JButton("Reset"); // 리셋 버튼
		btnReset.setBackground(new Color(248, 248, 255));
		btnReset.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnReset.setBounds(243, 164, 105, 27);
		contentPane.add(btnReset);

		JScrollPane scrollPane = new JScrollPane(); // JobPool을 담을 Scrollpane
		scrollPane.setBounds(585, 39, 569, 150);
		contentPane.add(scrollPane);

		gant_table = new JTable(); // 간트차트 테이블 선언
		gant_table.setBounds(0, 0, 1, 1);
		contentPane.add(gant_table);

		JScrollPane Ganntpanel = new JScrollPane(); // 간트차트 테이블을 담을 Scrollpane
		Ganntpanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS); // 가로축 스크롤이 항상 존재하도록 설정
		Ganntpanel.setBounds(71, 224, 1083, 220);
		contentPane.add(Ganntpanel);

		JScrollPane PN_panel = new JScrollPane(); // 프로세스 이름 테이블을 담을 Scrollpane
		PN_panel.setViewportBorder(null);
		PN_panel.setBounds(10, 224, 62, 220);
		contentPane.add(PN_panel);

		btnInsert.addActionListener(new ActionListener() { // Insert버튼 구현
			public void actionPerformed(ActionEvent e) {
				boolean canRun = true;

				String name = Name_text.getText(); // 프로세스 이름을 입력받음
				StringTokenizer st1 = new StringTokenizer(name, " ");

				String at = AT_text.getText(); // 도착시간을 입력받음
				char[] ats = new char[at.length()];
				ats = at.toCharArray();

				String bt = BT_text.getText(); // 실행시간을 입력받음
				char[] bts = new char[bt.length()];
				bts = bt.toCharArray();

				String quantum_tmp = TimeQuantum_text.getText(); // Time Quantum을 입력받음
				char[] qts = new char[quantum_tmp.length()];
				qts = quantum_tmp.toCharArray();
				/* 예외처리 */
				for (int i = 0; i < at.length(); ++i) {
					if (!('0' <= ats[i] && '9' >= ats[i] || ats[i] == ' ')) {
						JOptionPane.showMessageDialog(null, "Enter only positive N in AT\n press reset!", "Error",
								JOptionPane.WARNING_MESSAGE);
						canRun = false;
					}
				}
				for (int i = 0; i < bt.length(); ++i) {
					if (!('0' <= bts[i] && '9' >= bts[i] || bts[i] == ' ')) {
						JOptionPane.showMessageDialog(null, "Enter only positive N in BT\n press reset!", "Error",
								JOptionPane.WARNING_MESSAGE);
						canRun = false;
					}
				}

				if (quantum_tmp.length() > 1) {
					JOptionPane.showMessageDialog(null, "TQ has more value than one \n press reset!", "Error",
							JOptionPane.WARNING_MESSAGE);
					canRun = false;
				}

				if (quantum_tmp.length() == 1) {
					if (!('0' <= qts[0] && '9' >= qts[0] || qts[0] == ' ')) {
						JOptionPane.showMessageDialog(null, "Enter only positive N in Time Quantum\n press reset!",
								"Error", JOptionPane.WARNING_MESSAGE);
						canRun = false;
					}
				}

				if (canRun) {
					StringTokenizer st2 = new StringTokenizer(at, " ");
					StringTokenizer st3 = new StringTokenizer(bt, " "); // quantum에 값이 두개 들어갈 때 오류를 출력하는 화면

					if (algorithmBox.getSelectedIndex() == 1 || algorithmBox.getSelectedIndex() == 5) {// Round Robin 이
																										// 아닌경우
																										// timequantum
																										// 입력을 안 받음
						if (quantum_tmp.isEmpty() || Integer.parseInt(quantum_tmp) == 0) {
							JOptionPane.showMessageDialog(null, "TQ is not entered \n press reset!", "Error",
									JOptionPane.WARNING_MESSAGE);
							btnStart.hide();
						}

					} else {
						if (!quantum_tmp.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Do not enter TQ \n press reset!", "Error",
									JOptionPane.WARNING_MESSAGE);
							btnStart.hide();
						}
					}
					if (!quantum_tmp.isEmpty())
						quantum = Integer.parseInt(quantum_tmp);

					nameArr = new String[st1.countTokens()]; // 프로세스 이름을 받는 배열
					String[] arriveArr_tmp = new String[st2.countTokens()]; // 프로세스 도착시간들을 받는 배열
					String[] burstArr_tmp = new String[st3.countTokens()]; // 프로세스 수행시간들을 받는 배열

					rowData = new Object[st1.countTokens()][7]; // 프로세스 이름의 수와 도착시간, 실행시간이 다른건 예외처리 됨

					int ct1 = st1.countTokens(); // 토큰의 수를 저장
					int ct2 = st2.countTokens();
					int ct3 = st3.countTokens();
					N = ct1; // 프로세스의 수는 전체 토큰의 수

					if (ct1 != 0 && ct2 != 0 && ct3 != 0)
						btnStart.setEnabled(true);

					if ((ct1 == ct2 && ct1 == ct3 && ct2 == ct3)) { // process의 갯수와 bursttime arrivetime의 수가 같은지 다른지 검사
						for (int i = 0; i < ct1; i++) { // 입력받은 문자열을 토큰 분리하여 배열에 저장
							nameArr[i] = st1.nextToken();
							rowData[i][0] = nameArr[i];
						}

						for (int i = 0; i < ct2; i++) {
							arriveArr_tmp[i] = st2.nextToken();
							rowData[i][1] = arriveArr_tmp[i];
							arrival.add(i, Integer.parseInt(arriveArr_tmp[i]));
						}

						for (int i = 0; i < ct3; i++) {
							burstArr_tmp[i] = st3.nextToken();
							rowData[i][3] = burstArr_tmp[i];
							burst.add(i, Integer.parseInt(burstArr_tmp[i]));
							if (Integer.parseInt(burstArr_tmp[i]) == 0) { // 실행시간이 0인 프로세스는 오류처리
								JOptionPane.showMessageDialog(null, "Error! \n BT should not have '0' \n press reset!",
										"Error", JOptionPane.WARNING_MESSAGE);
								btnStart.hide();
							}
						}

						JobPool = new JTable(rowData, coulumnNames);
						scrollPane.setViewportView(JobPool);
					} else { // 토큰의 수가 서로 다르면 오류
						JOptionPane.showMessageDialog(null, "number_match error \n press reset!", "Error",
								JOptionPane.WARNING_MESSAGE);
						btnStart.hide();
					}
				}
			}
		});

		btnReset.addActionListener(new ActionListener() { // Reset버튼 구현
			public void actionPerformed(ActionEvent e) {
				btnStart.show();
				btnStart.setEnabled(false); // 리셋을 누르고 바로 스타트 버튼을 누를 수 없음

				Name_text.setText(""); // 텍스트 필드 초기화
				AT_text.setText("");
				BT_text.setText("");
				TimeQuantum_text.setText("");

				rowData = null; // JobPool 초기화
				JobPool = new JTable();
				scrollPane.setViewportView(JobPool);

				gant_x = null; // 간트차트 초기화
				gant_y = null;
				gant_table = new JTable();
				Ganntpanel.setViewportView(gant_table);

				PN_x = null; // 프로세스 이름 테이블 초기화
				PN_y = null;
				PN_table = new JTable();
				PN_panel.setViewportView(PN_table);
			}
		});

		btnStart.addActionListener(new ActionListener() { // Start버튼 구현
			public void actionPerformed(ActionEvent e) {

				if (algorithmBox.getSelectedIndex() == 0) { // JComboBox의 값에 따라서 해당 스케줄링 기법을 호출
					FCFS_func();
				}

				if (algorithmBox.getSelectedIndex() == 1) {
					RR_func();
				}
				if (algorithmBox.getSelectedIndex() == 2) {
					SPN_func();
				}
				if (algorithmBox.getSelectedIndex() == 3) {
					SRTN_func();
				}

				if (algorithmBox.getSelectedIndex() == 4) {
					HRRN_func();
				}
				if (algorithmBox.getSelectedIndex() == 5) {
					RRR_func();
				}

				int el = endArr.length;
				int el_mx = endArr[el - 1]; // 간트차트의 열에 사용할 인덱스

				gant_y = new String[el_mx]; // 간트차트의 열
				gant_x = new Object[nameArr.length][el_mx + 1]; // 간트차트의 행(데이터)

				PN_x = new String[nameArr.length][1]; // 프로세스 네임 테이블의 데이터
				PN_y = new String[1]; // 프로세스 네임 테이블의 열, 값은 PN 하나 뿐임
				PN_y[0] = ("PN");

				for (int i = 0; i < nameArr.length; i++) { // 프로세스 네임 테이블에 데이터 삽입
					PN_x[i][0] = nameArr[i];
				}

				for (int i = 0; i < el_mx; i++) { // 간트차트에 열 삽입, 열은 0부터 1씩 증가하는 초 단위를 의미
					gant_y[i] = Integer.toString(i);
				}

				for (int i = 0; i < nameArr.length; i++) { // 간트차트에 데이터 삽입
					for (int j = 0; j <= el_mx; j++) {
						gant_x[i][j] = ""; // 스케줄링 기법 실행전이므로 일단 빈 값을 넣음
					}
				}
				/* 간트차트 테이블에 색을 넣음 */
				Color color = new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0),
						(int) (Math.random() * 255.0)); // 랜덤한 색을 넣음
				gant_table = new JTable(gant_x, gant_y) {
					public Component prepareRenderer(TableCellRenderer r, int gant_x, int gant_y) {
						Component c = super.prepareRenderer(r, gant_x, gant_y);
						c.setBackground(Color.white);
						for (int k = 0; k < N; k++) {
							for (int i = 0; i < paintArr.length; i++) {
								if ((gant_x == k) && (paintArr[i] == k) && (gant_y == i)) {
									// 간트 차트의 행번호가 현재 실행중인 프로세스를 담는 배열의 값과 같으면서 초를 의미하는 간트의 열번호가 배열의 인덱스가 일치하는 경우
									c.setBackground(color); // 색을 삽입
								}
							}
						}
						return c;
					}
				};

				gant_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 간트테이블 크기조절을 불가능하게 설정
				Ganntpanel.setViewportView(gant_table);

				PN_table = new JTable(PN_x, PN_y);
				PN_panel.setViewportView(PN_table);

				for (int i = 0; i < N; i++) { // 알고리즘 실행 후 JobPool에 데이터를 삽입
					rowData[i][2] = (int) waitArr[i]; // 대기시간
					rowData[i][4] = (int) turnArr[i]; // 반환시간
					rowData[i][5] = NTTArr[i]; // 정규화
				}

				JobPool = new JTable(rowData, coulumnNames); // 완성된 JobPool을 불러옴
				scrollPane.setViewportView(JobPool);

				gant_table.setEnabled(false); // table의 값을 변경할 수 없음
				PN_table.setEnabled(false);
				JobPool.setEnabled(false);

				btnStart.setEnabled(false); // start버튼을 연속으로 누를 수 없음

			}
		});
	}

	// 알고리즘 시작
    public void FCFS_func() { 
        int ct[] = new int[N];     // 완료 시간
        int timeExecution[] = new int[N];
        int currentTime = 0;
        turnArr = new double [N];
        waitArr = new double [N];
        NTTArr = new double [N];
        endArr = new int [N];
        paintArr = new int [endArr[N-1]];
        int Processescount = N;
        int middleHold = 0;
        ArrayList process;
        ArrayList paint;
        ArrayList end;
        ArrayList cparrival,cpburst;
        
        cparrival = (ArrayList)arrival.clone();
        cpburst = (ArrayList)burst.clone();
        
        int excution, burstUpdate;
        
        process = new ArrayList();
        paint = new ArrayList();
        end = new ArrayList();
        
        while(Processescount>0) {      //총 실행해야할 프로세스 > 0
           for(int i = 0; i < N ; i++) {
              if((int)arrival.get(i) != -1 && (int)arrival.get(i) <= currentTime) {
                 process.add(i);            //현재 대기열에 진입한 프로세스  
                 arrival.set(i,-1);         //다시 대기열에 들어오지 않도록 -1값으로 변경
              }
              
           }
           if(process.isEmpty()) {            //모든프로세스가 완료되지않았고
              paint.add(-1);               //현재 대기중이거나 진행중이 프로세스가 없을 때
              currentTime++;
           }
           else {
              excution = (int)process.remove(0);      //가장 먼저들어온 프로세스 진행
              while((int)burst.get(excution)>0) {      //선택된 프로세스가 실행시간이 0보다 클 때
                 paint.add(excution);               //그리기 위한  paint에 그려준다.
                 currentTime++;                  //현재 시간 증가 
                 burstUpdate = (int)burst.get(excution)-1;   //시간이 지나면서 실행시간을 감소해준다.
                 burst.set(excution,burstUpdate);   //갱신된 실행시간을 넣어준다. 
              }
              end.add(currentTime);      //선택된 프로세스 실행시간이 0이되면 그 당시의 시간을 저장한다.
              ct[excution] = currentTime;
              Processescount--;      //총 실행해야할 프로세스 개수 감소
           }
           
        }
        
        
        turnArr = new double [N];            //반환시간을 저장한다.
        for(int i = 0 ; i < N ; i++) {
           turnArr[i] = ct[i] - (int)cparrival.get(i);   //완료시간-도착시간
        }
        
        NTTArr = new double[N];         //NTT를 구한다.
        for(int i = 0; i<N; i++){
           NTTArr[i] = Math.round((turnArr[i] / (int) cpburst.get(i))*100.0)/100.0;   //반환시간/실행시간
        }
        
        waitArr = new double[N];            //대기시간
        for(int i = 0; i<N; i++){
           timeExecution[i] = ct[i] - (int) cparrival.get(i);   //완료시간 - 도착시간
           middleHold = timeExecution[i] - (int) cpburst.get(i);   // (완료시간 -도착시간)-실행시간 = 대기시간
           waitArr[i] = middleHold;   
        }
        
        endArr = new int [end.size()];      //완료시간
        paintArr = new int [paint.size()];   //시각화를 위한 배열
        
         for(int i=0;i<end.size();i++)
            endArr[i]=(int)end.get(i);
         for(int i=0;i<paint.size();i++)
           paintArr[i]=(int)paint.get(i);
     }

    public void RR_func() {

        int currentTime, execution, q, endTime[], quantityProcesses, burstUpdate, timeExecution[];
        ArrayList process, cparrival, cpburst;
        double middleHold, turnaround;
        ArrayList end = new ArrayList();
        ArrayList paint = new ArrayList();

        process = new ArrayList(); // 프로세스를 나타내는 배열
        quantityProcesses = N; // 프로세스의 개수(N개)
        endTime = new int[N]; // 프로세스의 종료시간을 나타내는 배열
        timeExecution = new int[N]; // (프로세스의 종료시간 - ArrivalTime)를 나타내는 배열

        cparrival = (ArrayList) arrival.clone(); // ArrivalTime의 복사본
        cpburst = (ArrayList) burst.clone(); // BurstTime의 복사본
        currentTime = 0; // 현재시간

        while (quantityProcesses > 0) { // 프로세스가 있을 경우 반복문 수행(N개만큼)
           for (int i = 0; i < N; i++) {
              if ((int) arrival.get(i) != -1 && (int) arrival.get(i) <= currentTime) {
                 process.add(i); // 현재 대기열에 진입한 프로세스
                 arrival.set(i, -1); // 다시 대기열에 들어오지 않도록 -1값으로 변경
              }
           }

           if (process.isEmpty()) { // 처음 프로세스의 ArrivalTime < CurrentTime
              paint.add(-1); // 프로세스의 순서를 시작하는 부분
              currentTime++;
           } else { // 프로세스가 들어온 경우
              execution = (int) process.remove(0);
              q = quantum;

              while (q > 0 && (int) burst.get(execution) > 0) { // TimeQuantum과 BurstTime이 0보다 클 경우
                 paint.add(execution); // 프로세스의 순서를 추가
                 currentTime++;
                 q--;
                 burstUpdate = (int) burst.get(execution) - 1; 
                 burst.set(execution, burstUpdate); // BurstTime의 값을 최신화
              }
              end.add(currentTime); // TimeQuantum이 종료한 시간 측정

              if ((int) burst.get(execution) > 0) { // BurstTime-TimeQuantum을 실행하고도 BurstTime이 남았을경우
                 for (int i = 0; i < N; i++) {
                    if ((int) arrival.get(i) != -1 && (int) arrival.get(i) <= currentTime) {
                       process.add(i); // 현재 대기열에 진입한 프로세스
                       arrival.set(i, -1); // 다시 대기열에 들어오지 않도록 -1값으로 변경
                    }
                 }
                 process.add(execution); 
              } else { // BurstTime이 끝났을 때
                 endTime[execution] = currentTime; // 프로세스의 종료시간 = CurrentTime
                 quantityProcesses--; // 프로세스의 개수를 감소
              }
           }
        }

        middleHold = 0;

        turnArr = new double[N];
        for (int i = 0; i < N; i++) // TurnAroundTime = 프로세스의 종료시간 - ArrivalTime
           turnArr[i] = (int) endTime[i] - (int) cparrival.get(i);
        NTTArr = new double[N];
        for (int i = 0; i < N; i++) { // ntt = TurnAroundTime / BurstTime
           turnaround = (int) endTime[i] - (int) cparrival.get(i);
           NTTArr[i] = Math.round((turnaround / (int) cpburst.get(i))*100.0)/100.0;
        }
        waitArr = new double[N];
        for (int i = 0; i < N; i++) { // WatingTime = 프로세스의 종료시간 - ArrivalTime - BurstTime
           timeExecution[i] = endTime[i] - (int) cparrival.get(i);
           middleHold = timeExecution[i] - (int) cpburst.get(i);
           waitArr[i] = middleHold;
        }

        endArr = new int[end.size()];
        paintArr = new int[paint.size()];

        for (int i = 0; i < end.size(); i++) // 프로세스의 끝나는 시간 확인
           endArr[i] = (int) end.get(i);
        for (int i = 0; i < paint.size(); i++) // 프로세스를 시각화할 크기 확인
           paintArr[i] = (int) paint.get(i);
     }

	public void SPN_func() {
		int pid[] = new int[N];
		int ct[] = new int[N]; // ct means complete time
		int f[] = new int[N]; // f means it is flag it checks process is completed or not
		int st = 0, tot = 0;
		waitArr = new double[N];
		turnArr = new double[N];
		NTTArr = new double[N];
		ArrayList order = new ArrayList();
		ArrayList start = new ArrayList();
		ArrayList end = new ArrayList();
		ArrayList paint = new ArrayList();

		float avgwt = 0, avgta = 0;

		for (int i = 0; i < N; i++) {
			pid[i] = i + 1;
			f[i] = 0;
		}

		boolean a = true;

		while (true) {
			int c = N, min = 2147483647;// brust입력할 때 999이상 입력하면 오류남
			if (tot == N) // total no of process = completed process loop will be terminated
				break;

			for (int i = 0; i < N; i++) {
				if (((int) arrival.get(i) <= st) && (f[i] == 0) && ((int) burst.get(i) < min)) {
					min = (int) burst.get(i);
					c = i;
					order.add(i);
				}
			}
			if (c == N) {
				paint.add(-1);
				st++;
			} else {
				start.add(st);
				ct[c] = st + (int) burst.get(c);
				for (int i = st; i < ct[c]; i++)
					paint.add(c);
				st += (int) burst.get(c);
				end.add(st);
				turnArr[c] = ct[c] - (int) arrival.get(c);
				waitArr[c] = turnArr[c] - (int) burst.get(c);
				NTTArr[c] = Math.round((turnArr[c] / (int) burst.get(c)) * 100.0) / 100.0;
				f[c] = 1;
				tot++;
			}
		}

		endArr = new int[end.size()];
		paintArr = new int[paint.size()];

		for (int i = 0; i < end.size(); i++)
			endArr[i] = (int) end.get(i);
		for (int i = 0; i < paint.size(); i++)
			paintArr[i] = (int) paint.get(i);

		for (int i = 0; i < N; i++) {
			avgwt += waitArr[i];
			avgta += turnArr[i];
		}
	}

    public void SRTN_func() { 
        int f[] = new int[N];  // f는 flag, 프로세스가 끝났는지 확인한다. 
        int k[]= new int[N];   // k는 초기에 설정된 burst time을 저장한다.
        int i, st=0, tot=0;    // 필요한 변수 선언
        ArrayList end = new ArrayList();
        ArrayList paint = new ArrayList();
   
         for (i=0;i<N;i++)
         {
            k[i]= (int)burst.get(i);         //bt 값은 변경 됨으로  k이라는 배열을 생성하여 값을 저장 시킨다. 
            f[i]= 0;                     //초기 flag값은 0으로 초기화를 해준다. 
         }
         
         while(true){
            int min=2147483647,c=N;            //작은 burst time을 찾기 위한 min변수 생성
            if (tot==N)               //N개의 총 프로세스를 완료했을 때  무한반복문 종료
               break;
            
            for ( i=0;i<N;i++)
            {
               if (((int)arrival.get(i)<=st) && (f[i]==0) && ((int)burst.get(i)<min))   //현재 진행시간 st 보다 작은 수와 가장 작은 bust time일 때 
               {   
                  min=(int)burst.get(i);                                    
                  c=i;                                                //프로세스 실행한다. 
               }
            }
            
            if (c==N)               //실행중이지만 어떠한 프로세스도 실행중이 아닐 때
            {
               paint.add(-1);  	        //-1로 공백처리를 한다. 
               st++;               //시간은 진행
            }
            else
            {
               int a = (int)burst.get(c)-1;
               burst.set(c, a); //bt[c]--;
               paint.add(c);
               st++;
               if ((int)burst.get(c)==0)         //프로세스가 모든 실행시간을 했을 때
               {
                  end.add(st);               //현재시간을 완료시간에 넣는다.
                  f[c]=1;                     //완료한 프로젝트는 1로 넣어서 다시 돌아가지않도록한다.
                  tot++;                     //완료한 프로세스 개수 증가
               }
            }
         }
         
         turnArr = new double[N];   
         waitArr = new double[N];
         NTTArr = new double[N];
         
         for(i=0;i<N;i++)
         {
            turnArr[i] = (int)end.get(i) - (int)arrival.get(i);         //반환시간 = 완료시간 - 도착시간
            waitArr[i] = turnArr[i] - k[i];      // 대기시간 = 반환시간 - 실행시간
            NTTArr[i] = Math.round(((double)turnArr[i]/k[i])*100.0)/100.0;   // NTT = 반환시간/실행시간
         }
         
         endArr = new int [end.size()];      //완료시간
         paintArr = new int [paint.size()];   //시각화를 위한 배열
         
          for(int j=0;j<end.size();j++)
             endArr[j]=(int)end.get(j);
          for(int j=0;j<paint.size();j++)
            paintArr[j]=(int)paint.get(j);
         
     }

	public void HRRN_func() {
	      turnArr = new double[N];
	      waitArr = new double[N];
	      NTTArr = new double[N];
	      int currentTime, endTime[], quantityProcesses, burstUpdate;
	      ArrayList cparrival, cpburst;
	      double max_age, x;

	      ArrayList end = new ArrayList();
	      ArrayList paint = new ArrayList();

	      int a = 0; // 프로세스를 max_aging 순서를 나타낼 때 값을 저장할 a 선언. 0으로 초기화.
	      int temp_i = 0; // 프로세스를 aging 순서를 나타낼 때 값을 저장할 a 선언. 0으로 초기화.

	      quantityProcesses = N; // 프로세스의 개수(N개)
	      endTime = new int[N]; // 프로세스의 종료시간을 나타내는 배열

	      cparrival = (ArrayList) arrival.clone(); // ArrivalTime의 복사본
	      cpburst = (ArrayList) burst.clone(); // BurstTime의 복사본
	      currentTime = 0; // 현재시간

	      while (quantityProcesses > 0) {
	         int age_size = 0;
	         double age[];
	         age = new double[N];
	         for (int i = 0; i < N; i++) {
	            if ((int) arrival.get(i) != -1 && (int) arrival.get(i) <= currentTime) {
	               x = (double) (currentTime - (int) arrival.get(i) + (int) burst.get(i)) / (int) burst.get(i);
	               // aging(x) = (CurrentTime-ArrivalTime+BurstTime)/BurstTime = WaitingTime/BurstTime
	               age[i] = x;
	               age_size++; // aging의 크기 측정
	               temp_i = i; // aging의 순서는 i
	            }
	         }

	         if (age_size == 0) { // aging이 0일 때
	            paint.add(-1);
	            currentTime++;
	         } else if (age_size == 1) { // aging이 1일때(기다리는 프로세스가 1개)
	            arrival.set(temp_i, -1);
	            while ((int) burst.get(temp_i) > 0) { // BurstTime이 있을경우 반복문 수행
	               paint.add(temp_i); // 프로세스의 순서 추가
	               currentTime++;
	               burstUpdate = ((int) burst.get(temp_i)) - 1;
	               burst.set(temp_i, burstUpdate); // BurstTime의 값을 최신화
	            }
	            end.add(currentTime); // 종료시간을 end 배열에 추가
	            endTime[temp_i] = currentTime; // BurstTime의 종료시간
	            quantityProcesses--; // 프로세스의 개수를 감소
	         } else { // aging이 여러 개(기다리는 프로세스가 다수)
	            max_age = age[0];
	            for (int i = 1; i < age.length; i++) {
	               if (age[i] > max_age) { // aging이 가장 높은 프로세스를 찾는다
	                  max_age = age[i];
	                  a = i; // aging이 가장 높은 순서는 a
	               }
	            }
	            arrival.set(a, -1); // 다시 대기열에 들어오지 않도록 -1값으로 변경
	            while ((int) burst.get(a) > 0) { // BurstTime만큼 반복문 수행
	               paint.add(a); // 프로세스의 순서 추가
	               currentTime++;
	               burstUpdate = ((int) burst.get(a)) - 1;
	               burst.set(a, burstUpdate);
	            }
	            end.add(currentTime); // 종료시간을 end 배열에 추가  
	            endTime[a] = currentTime; // BurstTime의 종료시간
	            quantityProcesses--; // 프로세스의 개수를 감소
	         }
	      }

	      for (int i = 0; i < N; i++) {
	         turnArr[i] = (int) endTime[i] - (int) cparrival.get(i); // TurnAroundTime = 프로세스의 종료시간 - ArrivalTime
	         NTTArr[i] = Math.round((turnArr[i] / (int) cpburst.get(i)) * 100.0) / 100.0; // ntt = TurnAroundTime / BurstTime
	         waitArr[i] = turnArr[i] - (int) cpburst.get(i); // WatingTime = TurnAroundTime - BurstTime
	      }

	      endArr = new int[end.size()];
	      paintArr = new int[paint.size()];

	      for (int i = 0; i < end.size(); i++)
	         endArr[i] = (int) end.get(i); // 프로세스의 끝나는 시간 확인
	      for (int i = 0; i < paint.size(); i++)
	         paintArr[i] = (int) paint.get(i); // 프로세스를 시각화할 크기 확인
	   }

	public void RRR_func() { 
        
        int currentTime, execution, q, endTime[], quantityProcesses, burstUpdate;
        ArrayList process, cparrival, cpburst, exp_num;
        ArrayList end = new ArrayList();
        ArrayList paint = new ArrayList();
        
        process = new ArrayList();
        exp_num = new ArrayList();
        quantityProcesses = N;
        endTime = new int[N];
         
        for(int  i=0 ; i<N ; i++)
                  exp_num.add(1);
        
        cparrival = (ArrayList) arrival.clone();
        cpburst = (ArrayList) burst.clone();
        currentTime = 0;
        
        while(quantityProcesses > 0)
        {
                  for(int i =0 ; i <N ; i++)
                  {
                     if((int) arrival.get(i) != -1 && (int) arrival.get(i) <= currentTime)
                     {
                          process.add(i);
                          arrival.set(i, -1);
                      }
                  }
                  if(process.isEmpty())
                  {
                     paint.add(-1);
                     currentTime++;
                  }
                  else
                  {
                      execution = (int)process.remove(0);
                      q=quantum;
                      for(int i=0;i<((int) exp_num.get(execution))-1;i++) //exp_num가 2부터는 quantum의 지수승을 해준다
                         q *= quantum;
                      while((q > 0) && ((int) burst.get(execution)) > 0)
                      {
                         paint.add(execution);
                          currentTime++;
                          q--;
                          burstUpdate = (int) burst.get(execution) -1;
                          burst.set(execution, burstUpdate);
                      }
                      end.add(currentTime);//여기에 종료시간
                      int num_temp = (int)exp_num.get(execution);
                      exp_num.set(execution, num_temp+1);
                      if((int) burst.get(execution)>0)
                      {
                          for(int i=0; i<N ; i++)
                          {
                              if((int) arrival.get(i) != -1 && (int) arrival.get(i) <= currentTime)
                              {
                                  process.add(i);
                                  arrival.set(i, -1);
                              }
                          }
                          process.add(execution);
                      }
                      else
                      {
                          endTime[execution] = currentTime;
                          quantityProcesses--;
                      }
                  }
              }
              
              turnArr = new double[N];
              for(int i = 0; i<N; i++)
                 turnArr[i] = endTime[i] - (int)cparrival.get(i);
              
              waitArr = new double[N];
              for(int i = 0; i<N; i++)
                 waitArr[i] = endTime[i] - (int) cparrival.get(i) - (int) cpburst.get(i);
              
              NTTArr = new double[N];
              for(int i = 0; i<N; i++)
                 NTTArr[i] = Math.round((turnArr[i] / (int)cpburst.get(i))*100.0)/100.0;
             endArr = new int [end.size()];
             for(int i=0;i<end.size();i++)
                 endArr[i]=(int)end.get(i);
             
             paintArr = new int [paint.size()];
              for(int i=0;i<paint.size();i++)
                 paintArr[i]=(int)paint.get(i);

          }
}