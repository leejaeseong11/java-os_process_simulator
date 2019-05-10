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

	private JPanel contentPane; // �������� ��� ���� �г�
	private JTextField Name_text; // ���� ����� ���� �ؽ�Ʈ�ʵ�
	private JTextField AT_text;
	private JTextField BT_text;
	private JTextField TimeQuantum_text;
	private JTable JobPool; // �� ����� ���� ���̺�
	private JTable gant_table; // ��Ʈ��Ʈ ����� ���� ���̺�
	private JTable PN_table; // ��Ʈ��Ʈ�� ���μ��� �̸��� ����ϱ� ���� ���̺�

	public static Object[][] rowData = null; // Job Pool ���̺� �ε���
	public static String coulumnNames[] = { "Name", "AT", "WT", "BT", "TT", "NTT" };
	public static Object[][] gant_x = null; // ��Ʈ��Ʈ ���̺� �ε���
	public static String gant_y[] = null;
	public static Object[][] PN_x = null; // ��Ʈ��Ʈ ���μ��� �̸� ���̺� �ε���
	public static String PN_y[] = null;
	public static String[] nameArr = null; // ���μ��� �̸��� �޴� �迭
	public static ArrayList arrival = new ArrayList(); // ���μ��� �����ð����� �޴� �迭����Ʈ
	public static ArrayList burst = new ArrayList(); // ���μ��� ����ð����� �޴� �迭����Ʈ
	public static double[] waitArr = null; // ���μ��� ���ð����� �޴� �迭
	public static double[] turnArr = null; // ���μ��� ��ȯ�ð����� �޴� �迭
	public static double[] NTTArr = null; // ���μ����� ����ȭ ���� �޴� �迭
	public static int[] endArr = null; // ���μ����� ������ �ð��� �޴� �迭
	public static int[] paintArr = null; // ���μ����� ������ ������ �޾� ��Ʈ��Ʈ���� ����� �迭
	public static int N = 0; // ���μ��� ��
	public static int quantum = 0; // quantum ��

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { // java swing�� ���� �̺�Ʈ ó��
			public void run() {
				try {
					ProcessScheduler frame = new ProcessScheduler(); // gui�� ȣ����
					frame.setVisible(true);
					frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ProcessScheduler() {
		setTitle("Process Scheduler"); // ����â�� �̸�
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // â�� ������ �޸𸮸� ����
		setBounds(100, 100, 1190, 503);
		contentPane = new JPanel(); // �г� ����
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // �����ο� ��ġ�� ���� EmptyBorder�� ����
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblAlgorithms = new JLabel("Algorithm :"); // �󺧸�
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

		JPanel panel = new JPanel(); // �ڸ�Ʈ�� ���� �г� ����
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(356, 39, 223, 150);
		contentPane.add(panel);

		JLabel lblNewLabel_3 = new JLabel("Developers'_commentary");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		lblNewLabel_3.setBounds(356, 14, 201, 15);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel(
				"<html>Process_name, BT, AT <br>\uC785\uB825\uC2DC \uB744\uC5B4\uC4F0\uAE30\uB85C \uAD6C\uBD84\uD574 \uC8FC\uC138\uC694 <br>PN,AT,BT\uB294 insert, WT,TT,NTT\uB294<br> start \uBC84\uD2BC\uC744 \uB204\uB974\uBA74 \uCD9C\uB825\uB429\uB2C8\uB2E4.<br>PN &nbsp p1  p2 p3 p4 <br>AT &nbsp 1 &nbsp 2 &nbsp 3 &nbsp 4 <br>BT &nbsp 3 &nbsp 4 &nbsp 2 &nbsp 2");
		lblNewLabel_4.setFont(new Font("���� ���", Font.BOLD, 14));
		panel.add(lblNewLabel_4);

		JComboBox algorithmBox = new JComboBox(); // �˰��� ����
		algorithmBox.setBackground(new Color(248, 248, 255));
		algorithmBox.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		algorithmBox.setModel(new DefaultComboBoxModel(new String[] { "FCFS", "RR", "SPN", "SRTN", "HRRN", "RRR" }));
		algorithmBox.setToolTipText("");
		algorithmBox.setBounds(136, 9, 212, 24);
		contentPane.add(algorithmBox);

		Name_text = new JTextField(); // ���μ��� �̸�
		Name_text.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		Name_text.setBounds(136, 43, 212, 24);
		contentPane.add(Name_text);
		Name_text.setColumns(10);

		AT_text = new JTextField(); // �����ð�
		AT_text.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		AT_text.setBounds(136, 73, 212, 24);
		contentPane.add(AT_text);
		AT_text.setColumns(10);

		BT_text = new JTextField(); // ����ð�
		BT_text.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		BT_text.setBounds(136, 103, 212, 24);
		contentPane.add(BT_text);
		BT_text.setColumns(10);

		TimeQuantum_text = new JTextField(); // Time Quantum
		TimeQuantum_text.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		TimeQuantum_text.setBounds(136, 136, 212, 24);
		contentPane.add(TimeQuantum_text);
		TimeQuantum_text.setColumns(10);

		JButton btnInsert = new JButton("Insert"); // ���� ��ư
		btnInsert.setBackground(new Color(248, 248, 255));
		btnInsert.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnInsert.setBounds(14, 164, 105, 27);
		contentPane.add(btnInsert);

		JButton btnStart = new JButton("Start"); // ���� ��ư
		btnStart.setBackground(new Color(248, 248, 255));
		btnStart.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnStart.setBounds(129, 164, 105, 27);
		contentPane.add(btnStart);
		btnStart.setEnabled(false);

		JButton btnReset = new JButton("Reset"); // ���� ��ư
		btnReset.setBackground(new Color(248, 248, 255));
		btnReset.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnReset.setBounds(243, 164, 105, 27);
		contentPane.add(btnReset);

		JScrollPane scrollPane = new JScrollPane(); // JobPool�� ���� Scrollpane
		scrollPane.setBounds(585, 39, 569, 150);
		contentPane.add(scrollPane);

		gant_table = new JTable(); // ��Ʈ��Ʈ ���̺� ����
		gant_table.setBounds(0, 0, 1, 1);
		contentPane.add(gant_table);

		JScrollPane Ganntpanel = new JScrollPane(); // ��Ʈ��Ʈ ���̺��� ���� Scrollpane
		Ganntpanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS); // ������ ��ũ���� �׻� �����ϵ��� ����
		Ganntpanel.setBounds(71, 224, 1083, 220);
		contentPane.add(Ganntpanel);

		JScrollPane PN_panel = new JScrollPane(); // ���μ��� �̸� ���̺��� ���� Scrollpane
		PN_panel.setViewportBorder(null);
		PN_panel.setBounds(10, 224, 62, 220);
		contentPane.add(PN_panel);

		btnInsert.addActionListener(new ActionListener() { // Insert��ư ����
			public void actionPerformed(ActionEvent e) {
				boolean canRun = true;

				String name = Name_text.getText(); // ���μ��� �̸��� �Է¹���
				StringTokenizer st1 = new StringTokenizer(name, " ");

				String at = AT_text.getText(); // �����ð��� �Է¹���
				char[] ats = new char[at.length()];
				ats = at.toCharArray();

				String bt = BT_text.getText(); // ����ð��� �Է¹���
				char[] bts = new char[bt.length()];
				bts = bt.toCharArray();

				String quantum_tmp = TimeQuantum_text.getText(); // Time Quantum�� �Է¹���
				char[] qts = new char[quantum_tmp.length()];
				qts = quantum_tmp.toCharArray();
				/* ����ó�� */
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
					StringTokenizer st3 = new StringTokenizer(bt, " "); // quantum�� ���� �ΰ� �� �� ������ ����ϴ� ȭ��

					if (algorithmBox.getSelectedIndex() == 1 || algorithmBox.getSelectedIndex() == 5) {// Round Robin ��
																										// �ƴѰ��
																										// timequantum
																										// �Է��� �� ����
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

					nameArr = new String[st1.countTokens()]; // ���μ��� �̸��� �޴� �迭
					String[] arriveArr_tmp = new String[st2.countTokens()]; // ���μ��� �����ð����� �޴� �迭
					String[] burstArr_tmp = new String[st3.countTokens()]; // ���μ��� ����ð����� �޴� �迭

					rowData = new Object[st1.countTokens()][7]; // ���μ��� �̸��� ���� �����ð�, ����ð��� �ٸ��� ����ó�� ��

					int ct1 = st1.countTokens(); // ��ū�� ���� ����
					int ct2 = st2.countTokens();
					int ct3 = st3.countTokens();
					N = ct1; // ���μ����� ���� ��ü ��ū�� ��

					if (ct1 != 0 && ct2 != 0 && ct3 != 0)
						btnStart.setEnabled(true);

					if ((ct1 == ct2 && ct1 == ct3 && ct2 == ct3)) { // process�� ������ bursttime arrivetime�� ���� ������ �ٸ��� �˻�
						for (int i = 0; i < ct1; i++) { // �Է¹��� ���ڿ��� ��ū �и��Ͽ� �迭�� ����
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
							if (Integer.parseInt(burstArr_tmp[i]) == 0) { // ����ð��� 0�� ���μ����� ����ó��
								JOptionPane.showMessageDialog(null, "Error! \n BT should not have '0' \n press reset!",
										"Error", JOptionPane.WARNING_MESSAGE);
								btnStart.hide();
							}
						}

						JobPool = new JTable(rowData, coulumnNames);
						scrollPane.setViewportView(JobPool);
					} else { // ��ū�� ���� ���� �ٸ��� ����
						JOptionPane.showMessageDialog(null, "number_match error \n press reset!", "Error",
								JOptionPane.WARNING_MESSAGE);
						btnStart.hide();
					}
				}
			}
		});

		btnReset.addActionListener(new ActionListener() { // Reset��ư ����
			public void actionPerformed(ActionEvent e) {
				btnStart.show();
				btnStart.setEnabled(false); // ������ ������ �ٷ� ��ŸƮ ��ư�� ���� �� ����

				Name_text.setText(""); // �ؽ�Ʈ �ʵ� �ʱ�ȭ
				AT_text.setText("");
				BT_text.setText("");
				TimeQuantum_text.setText("");

				rowData = null; // JobPool �ʱ�ȭ
				JobPool = new JTable();
				scrollPane.setViewportView(JobPool);

				gant_x = null; // ��Ʈ��Ʈ �ʱ�ȭ
				gant_y = null;
				gant_table = new JTable();
				Ganntpanel.setViewportView(gant_table);

				PN_x = null; // ���μ��� �̸� ���̺� �ʱ�ȭ
				PN_y = null;
				PN_table = new JTable();
				PN_panel.setViewportView(PN_table);
			}
		});

		btnStart.addActionListener(new ActionListener() { // Start��ư ����
			public void actionPerformed(ActionEvent e) {

				if (algorithmBox.getSelectedIndex() == 0) { // JComboBox�� ���� ���� �ش� �����ٸ� ����� ȣ��
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
				int el_mx = endArr[el - 1]; // ��Ʈ��Ʈ�� ���� ����� �ε���

				gant_y = new String[el_mx]; // ��Ʈ��Ʈ�� ��
				gant_x = new Object[nameArr.length][el_mx + 1]; // ��Ʈ��Ʈ�� ��(������)

				PN_x = new String[nameArr.length][1]; // ���μ��� ���� ���̺��� ������
				PN_y = new String[1]; // ���μ��� ���� ���̺��� ��, ���� PN �ϳ� ����
				PN_y[0] = ("PN");

				for (int i = 0; i < nameArr.length; i++) { // ���μ��� ���� ���̺� ������ ����
					PN_x[i][0] = nameArr[i];
				}

				for (int i = 0; i < el_mx; i++) { // ��Ʈ��Ʈ�� �� ����, ���� 0���� 1�� �����ϴ� �� ������ �ǹ�
					gant_y[i] = Integer.toString(i);
				}

				for (int i = 0; i < nameArr.length; i++) { // ��Ʈ��Ʈ�� ������ ����
					for (int j = 0; j <= el_mx; j++) {
						gant_x[i][j] = ""; // �����ٸ� ��� �������̹Ƿ� �ϴ� �� ���� ����
					}
				}
				/* ��Ʈ��Ʈ ���̺� ���� ���� */
				Color color = new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0),
						(int) (Math.random() * 255.0)); // ������ ���� ����
				gant_table = new JTable(gant_x, gant_y) {
					public Component prepareRenderer(TableCellRenderer r, int gant_x, int gant_y) {
						Component c = super.prepareRenderer(r, gant_x, gant_y);
						c.setBackground(Color.white);
						for (int k = 0; k < N; k++) {
							for (int i = 0; i < paintArr.length; i++) {
								if ((gant_x == k) && (paintArr[i] == k) && (gant_y == i)) {
									// ��Ʈ ��Ʈ�� ���ȣ�� ���� �������� ���μ����� ��� �迭�� ���� �����鼭 �ʸ� �ǹ��ϴ� ��Ʈ�� ����ȣ�� �迭�� �ε����� ��ġ�ϴ� ���
									c.setBackground(color); // ���� ����
								}
							}
						}
						return c;
					}
				};

				gant_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ��Ʈ���̺� ũ�������� �Ұ����ϰ� ����
				Ganntpanel.setViewportView(gant_table);

				PN_table = new JTable(PN_x, PN_y);
				PN_panel.setViewportView(PN_table);

				for (int i = 0; i < N; i++) { // �˰��� ���� �� JobPool�� �����͸� ����
					rowData[i][2] = (int) waitArr[i]; // ���ð�
					rowData[i][4] = (int) turnArr[i]; // ��ȯ�ð�
					rowData[i][5] = NTTArr[i]; // ����ȭ
				}

				JobPool = new JTable(rowData, coulumnNames); // �ϼ��� JobPool�� �ҷ���
				scrollPane.setViewportView(JobPool);

				gant_table.setEnabled(false); // table�� ���� ������ �� ����
				PN_table.setEnabled(false);
				JobPool.setEnabled(false);

				btnStart.setEnabled(false); // start��ư�� �������� ���� �� ����

			}
		});
	}

	// �˰��� ����
    public void FCFS_func() { 
        int ct[] = new int[N];     // �Ϸ� �ð�
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
        
        while(Processescount>0) {      //�� �����ؾ��� ���μ��� > 0
           for(int i = 0; i < N ; i++) {
              if((int)arrival.get(i) != -1 && (int)arrival.get(i) <= currentTime) {
                 process.add(i);            //���� ��⿭�� ������ ���μ���  
                 arrival.set(i,-1);         //�ٽ� ��⿭�� ������ �ʵ��� -1������ ����
              }
              
           }
           if(process.isEmpty()) {            //������μ����� �Ϸ�����ʾҰ�
              paint.add(-1);               //���� ������̰ų� �������� ���μ����� ���� ��
              currentTime++;
           }
           else {
              excution = (int)process.remove(0);      //���� �������� ���μ��� ����
              while((int)burst.get(excution)>0) {      //���õ� ���μ����� ����ð��� 0���� Ŭ ��
                 paint.add(excution);               //�׸��� ����  paint�� �׷��ش�.
                 currentTime++;                  //���� �ð� ���� 
                 burstUpdate = (int)burst.get(excution)-1;   //�ð��� �����鼭 ����ð��� �������ش�.
                 burst.set(excution,burstUpdate);   //���ŵ� ����ð��� �־��ش�. 
              }
              end.add(currentTime);      //���õ� ���μ��� ����ð��� 0�̵Ǹ� �� ����� �ð��� �����Ѵ�.
              ct[excution] = currentTime;
              Processescount--;      //�� �����ؾ��� ���μ��� ���� ����
           }
           
        }
        
        
        turnArr = new double [N];            //��ȯ�ð��� �����Ѵ�.
        for(int i = 0 ; i < N ; i++) {
           turnArr[i] = ct[i] - (int)cparrival.get(i);   //�Ϸ�ð�-�����ð�
        }
        
        NTTArr = new double[N];         //NTT�� ���Ѵ�.
        for(int i = 0; i<N; i++){
           NTTArr[i] = Math.round((turnArr[i] / (int) cpburst.get(i))*100.0)/100.0;   //��ȯ�ð�/����ð�
        }
        
        waitArr = new double[N];            //���ð�
        for(int i = 0; i<N; i++){
           timeExecution[i] = ct[i] - (int) cparrival.get(i);   //�Ϸ�ð� - �����ð�
           middleHold = timeExecution[i] - (int) cpburst.get(i);   // (�Ϸ�ð� -�����ð�)-����ð� = ���ð�
           waitArr[i] = middleHold;   
        }
        
        endArr = new int [end.size()];      //�Ϸ�ð�
        paintArr = new int [paint.size()];   //�ð�ȭ�� ���� �迭
        
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

        process = new ArrayList(); // ���μ����� ��Ÿ���� �迭
        quantityProcesses = N; // ���μ����� ����(N��)
        endTime = new int[N]; // ���μ����� ����ð��� ��Ÿ���� �迭
        timeExecution = new int[N]; // (���μ����� ����ð� - ArrivalTime)�� ��Ÿ���� �迭

        cparrival = (ArrayList) arrival.clone(); // ArrivalTime�� ���纻
        cpburst = (ArrayList) burst.clone(); // BurstTime�� ���纻
        currentTime = 0; // ����ð�

        while (quantityProcesses > 0) { // ���μ����� ���� ��� �ݺ��� ����(N����ŭ)
           for (int i = 0; i < N; i++) {
              if ((int) arrival.get(i) != -1 && (int) arrival.get(i) <= currentTime) {
                 process.add(i); // ���� ��⿭�� ������ ���μ���
                 arrival.set(i, -1); // �ٽ� ��⿭�� ������ �ʵ��� -1������ ����
              }
           }

           if (process.isEmpty()) { // ó�� ���μ����� ArrivalTime < CurrentTime
              paint.add(-1); // ���μ����� ������ �����ϴ� �κ�
              currentTime++;
           } else { // ���μ����� ���� ���
              execution = (int) process.remove(0);
              q = quantum;

              while (q > 0 && (int) burst.get(execution) > 0) { // TimeQuantum�� BurstTime�� 0���� Ŭ ���
                 paint.add(execution); // ���μ����� ������ �߰�
                 currentTime++;
                 q--;
                 burstUpdate = (int) burst.get(execution) - 1; 
                 burst.set(execution, burstUpdate); // BurstTime�� ���� �ֽ�ȭ
              }
              end.add(currentTime); // TimeQuantum�� ������ �ð� ����

              if ((int) burst.get(execution) > 0) { // BurstTime-TimeQuantum�� �����ϰ� BurstTime�� ���������
                 for (int i = 0; i < N; i++) {
                    if ((int) arrival.get(i) != -1 && (int) arrival.get(i) <= currentTime) {
                       process.add(i); // ���� ��⿭�� ������ ���μ���
                       arrival.set(i, -1); // �ٽ� ��⿭�� ������ �ʵ��� -1������ ����
                    }
                 }
                 process.add(execution); 
              } else { // BurstTime�� ������ ��
                 endTime[execution] = currentTime; // ���μ����� ����ð� = CurrentTime
                 quantityProcesses--; // ���μ����� ������ ����
              }
           }
        }

        middleHold = 0;

        turnArr = new double[N];
        for (int i = 0; i < N; i++) // TurnAroundTime = ���μ����� ����ð� - ArrivalTime
           turnArr[i] = (int) endTime[i] - (int) cparrival.get(i);
        NTTArr = new double[N];
        for (int i = 0; i < N; i++) { // ntt = TurnAroundTime / BurstTime
           turnaround = (int) endTime[i] - (int) cparrival.get(i);
           NTTArr[i] = Math.round((turnaround / (int) cpburst.get(i))*100.0)/100.0;
        }
        waitArr = new double[N];
        for (int i = 0; i < N; i++) { // WatingTime = ���μ����� ����ð� - ArrivalTime - BurstTime
           timeExecution[i] = endTime[i] - (int) cparrival.get(i);
           middleHold = timeExecution[i] - (int) cpburst.get(i);
           waitArr[i] = middleHold;
        }

        endArr = new int[end.size()];
        paintArr = new int[paint.size()];

        for (int i = 0; i < end.size(); i++) // ���μ����� ������ �ð� Ȯ��
           endArr[i] = (int) end.get(i);
        for (int i = 0; i < paint.size(); i++) // ���μ����� �ð�ȭ�� ũ�� Ȯ��
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
			int c = N, min = 2147483647;// brust�Է��� �� 999�̻� �Է��ϸ� ������
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
        int f[] = new int[N];  // f�� flag, ���μ����� �������� Ȯ���Ѵ�. 
        int k[]= new int[N];   // k�� �ʱ⿡ ������ burst time�� �����Ѵ�.
        int i, st=0, tot=0;    // �ʿ��� ���� ����
        ArrayList end = new ArrayList();
        ArrayList paint = new ArrayList();
   
         for (i=0;i<N;i++)
         {
            k[i]= (int)burst.get(i);         //bt ���� ���� ������  k�̶�� �迭�� �����Ͽ� ���� ���� ��Ų��. 
            f[i]= 0;                     //�ʱ� flag���� 0���� �ʱ�ȭ�� ���ش�. 
         }
         
         while(true){
            int min=2147483647,c=N;            //���� burst time�� ã�� ���� min���� ����
            if (tot==N)               //N���� �� ���μ����� �Ϸ����� ��  ���ѹݺ��� ����
               break;
            
            for ( i=0;i<N;i++)
            {
               if (((int)arrival.get(i)<=st) && (f[i]==0) && ((int)burst.get(i)<min))   //���� ����ð� st ���� ���� ���� ���� ���� bust time�� �� 
               {   
                  min=(int)burst.get(i);                                    
                  c=i;                                                //���μ��� �����Ѵ�. 
               }
            }
            
            if (c==N)               //������������ ��� ���μ����� �������� �ƴ� ��
            {
               paint.add(-1);  	        //-1�� ����ó���� �Ѵ�. 
               st++;               //�ð��� ����
            }
            else
            {
               int a = (int)burst.get(c)-1;
               burst.set(c, a); //bt[c]--;
               paint.add(c);
               st++;
               if ((int)burst.get(c)==0)         //���μ����� ��� ����ð��� ���� ��
               {
                  end.add(st);               //����ð��� �Ϸ�ð��� �ִ´�.
                  f[c]=1;                     //�Ϸ��� ������Ʈ�� 1�� �־ �ٽ� ���ư����ʵ����Ѵ�.
                  tot++;                     //�Ϸ��� ���μ��� ���� ����
               }
            }
         }
         
         turnArr = new double[N];   
         waitArr = new double[N];
         NTTArr = new double[N];
         
         for(i=0;i<N;i++)
         {
            turnArr[i] = (int)end.get(i) - (int)arrival.get(i);         //��ȯ�ð� = �Ϸ�ð� - �����ð�
            waitArr[i] = turnArr[i] - k[i];      // ���ð� = ��ȯ�ð� - ����ð�
            NTTArr[i] = Math.round(((double)turnArr[i]/k[i])*100.0)/100.0;   // NTT = ��ȯ�ð�/����ð�
         }
         
         endArr = new int [end.size()];      //�Ϸ�ð�
         paintArr = new int [paint.size()];   //�ð�ȭ�� ���� �迭
         
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

	      int a = 0; // ���μ����� max_aging ������ ��Ÿ�� �� ���� ������ a ����. 0���� �ʱ�ȭ.
	      int temp_i = 0; // ���μ����� aging ������ ��Ÿ�� �� ���� ������ a ����. 0���� �ʱ�ȭ.

	      quantityProcesses = N; // ���μ����� ����(N��)
	      endTime = new int[N]; // ���μ����� ����ð��� ��Ÿ���� �迭

	      cparrival = (ArrayList) arrival.clone(); // ArrivalTime�� ���纻
	      cpburst = (ArrayList) burst.clone(); // BurstTime�� ���纻
	      currentTime = 0; // ����ð�

	      while (quantityProcesses > 0) {
	         int age_size = 0;
	         double age[];
	         age = new double[N];
	         for (int i = 0; i < N; i++) {
	            if ((int) arrival.get(i) != -1 && (int) arrival.get(i) <= currentTime) {
	               x = (double) (currentTime - (int) arrival.get(i) + (int) burst.get(i)) / (int) burst.get(i);
	               // aging(x) = (CurrentTime-ArrivalTime+BurstTime)/BurstTime = WaitingTime/BurstTime
	               age[i] = x;
	               age_size++; // aging�� ũ�� ����
	               temp_i = i; // aging�� ������ i
	            }
	         }

	         if (age_size == 0) { // aging�� 0�� ��
	            paint.add(-1);
	            currentTime++;
	         } else if (age_size == 1) { // aging�� 1�϶�(��ٸ��� ���μ����� 1��)
	            arrival.set(temp_i, -1);
	            while ((int) burst.get(temp_i) > 0) { // BurstTime�� ������� �ݺ��� ����
	               paint.add(temp_i); // ���μ����� ���� �߰�
	               currentTime++;
	               burstUpdate = ((int) burst.get(temp_i)) - 1;
	               burst.set(temp_i, burstUpdate); // BurstTime�� ���� �ֽ�ȭ
	            }
	            end.add(currentTime); // ����ð��� end �迭�� �߰�
	            endTime[temp_i] = currentTime; // BurstTime�� ����ð�
	            quantityProcesses--; // ���μ����� ������ ����
	         } else { // aging�� ���� ��(��ٸ��� ���μ����� �ټ�)
	            max_age = age[0];
	            for (int i = 1; i < age.length; i++) {
	               if (age[i] > max_age) { // aging�� ���� ���� ���μ����� ã�´�
	                  max_age = age[i];
	                  a = i; // aging�� ���� ���� ������ a
	               }
	            }
	            arrival.set(a, -1); // �ٽ� ��⿭�� ������ �ʵ��� -1������ ����
	            while ((int) burst.get(a) > 0) { // BurstTime��ŭ �ݺ��� ����
	               paint.add(a); // ���μ����� ���� �߰�
	               currentTime++;
	               burstUpdate = ((int) burst.get(a)) - 1;
	               burst.set(a, burstUpdate);
	            }
	            end.add(currentTime); // ����ð��� end �迭�� �߰�  
	            endTime[a] = currentTime; // BurstTime�� ����ð�
	            quantityProcesses--; // ���μ����� ������ ����
	         }
	      }

	      for (int i = 0; i < N; i++) {
	         turnArr[i] = (int) endTime[i] - (int) cparrival.get(i); // TurnAroundTime = ���μ����� ����ð� - ArrivalTime
	         NTTArr[i] = Math.round((turnArr[i] / (int) cpburst.get(i)) * 100.0) / 100.0; // ntt = TurnAroundTime / BurstTime
	         waitArr[i] = turnArr[i] - (int) cpburst.get(i); // WatingTime = TurnAroundTime - BurstTime
	      }

	      endArr = new int[end.size()];
	      paintArr = new int[paint.size()];

	      for (int i = 0; i < end.size(); i++)
	         endArr[i] = (int) end.get(i); // ���μ����� ������ �ð� Ȯ��
	      for (int i = 0; i < paint.size(); i++)
	         paintArr[i] = (int) paint.get(i); // ���μ����� �ð�ȭ�� ũ�� Ȯ��
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
                      for(int i=0;i<((int) exp_num.get(execution))-1;i++) //exp_num�� 2���ʹ� quantum�� �������� ���ش�
                         q *= quantum;
                      while((q > 0) && ((int) burst.get(execution)) > 0)
                      {
                         paint.add(execution);
                          currentTime++;
                          q--;
                          burstUpdate = (int) burst.get(execution) -1;
                          burst.set(execution, burstUpdate);
                      }
                      end.add(currentTime);//���⿡ ����ð�
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