package Client;

import Client.*;
import BankConsulting.FrameMain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ClientFrame extends JDialog implements ActionListener {

	JPanel pw = new JPanel(new GridLayout(4, 1)); //label
	JPanel pc = new JPanel(new GridLayout(4, 1));  //textfield
	JPanel ps = new JPanel();  //button

	JLabel lable_cnum = new JLabel("고객 코드");
	JLabel lable_pnum = new JLabel("주민번호");
	JLabel lable_name = new JLabel("이름");
	JLabel label_ph = new JLabel("전화번호");

	JTextField cnum = new JTextField();
	JTextField pnum = new JTextField();
	JTextField name = new JTextField();
	JTextField ph = new JTextField();

	JButton confirm;
	JButton reset = new JButton("취소");

	ClientJTableExam c;

	JPanel codeCkP = new JPanel(new BorderLayout());
	JButton codeCkB = new JButton("코드 중복 검사");

	ClientDAO dao = new ClientDAO();
	ClientInsert insert = new ClientInsert();
	ClientDelete delete = new ClientDelete();
	ClientUpdate update = new ClientUpdate();
	ClientVo vo = new ClientVo();
	
	//생성자 함수
	public ClientFrame(ClientJTableExam c, String index) {
		super(c, "직원 정보");
		this.c = c;

		// 등록과수정 버튼 중 무엇을 띄울지 정하기
		if (index.contentEquals("등록")) {
			confirm = new JButton(index);
		} 
		else {
			confirm = new JButton("수정");

			// text박스에 선택된 레코드의 정보 넣기
			int row = c.jt.getSelectedRow();
			cnum.setText(c.jt.getValueAt(row, 0).toString());
			pnum.setText(c.jt.getValueAt(row, 2).toString());
			name.setText(c.jt.getValueAt(row, 1).toString());

			// code 관련 활성화/비활성화
			// code text 박스 비활성화
			cnum.setEditable(false);
			// codeCheck버튼 비활성화
			codeCkB.setEnabled(false);
		}
		
		//label 추가하기
		pw.add(lable_cnum);
        pw.add(lable_pnum);
        pw.add(lable_name);
        pw.add(label_ph);
        
        codeCkP.add(cnum,"Center");
        codeCkP.add(codeCkB,"East");
        
        //TextField 추가
        pc.add(cnum);
        pc.add(pnum);
        pc.add(name);
        pc.add(ph);

        //button 추가
        ps.add(confirm);
        ps.add(reset);
        
        //panel 추가
        add(pw,"West");
        add(pc,"Center");
        add(ps,"South");
        
        setSize(300,250);
        setVisible(true);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        //버튼 이벤트 등록
        confirm.addActionListener(this); //가입과 수정 이벤트
        reset.addActionListener(this);  //취소 이벤트
        codeCkB.addActionListener(this);  //code 중복 체크 이벤트
	}

	
	//버튼 이벤트 기능 구현
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//이벤트 주체에 대한 label 가져오기
		String btnLabel = e.getActionCommand();
		
		if(btnLabel.contentEquals("등록")) {
			if(insert.clientInsert(this) > 0) {
				messageBox(this,name.getText()+" 고객 등록 성공");
				dispose();
				
				//모든 레코드 가져와서 DefaultTableModel에 올리기
				vo.clientSelect(c.dt);
				
				if(c.dt.getRowCount() > 0)
					c.jt.setRowSelectionInterval(0, 0);//첫번째 행 선택
			}
			else {
				messageBox(this,"등록되지 않음");
			}
		}
		else if(btnLabel.equals("수정")) {
			if(update.clientUpdate(this)>0) {
				messageBox(this,"수정 완료");
				dispose();
				
				//모든 레코드 가져와서 DefaultTableModel에 올리기
				vo.clientSelect(c.dt);
				
				if(c.dt.getRowCount() > 0)
					c.jt.setRowSelectionInterval(0, 0);//첫번째 행 선택
			}
			else {
				messageBox(this,"수정되지 않음");
			}
		}
		else if(btnLabel.equals("취소")) {
			dispose();
		}
		else if(btnLabel.equals("코드 중복 검사")) {
			//code 박스에 값이 없으면 메시지 출력 있으면 db 연동
			if(cnum.getText().trim().equals("")) {
				messageBox(this,"코드 입력하세요");
				cnum.requestFocus(); //포커스 이동
			}
			else {
				if(dao.getCodebycheck(cnum.getText())) {
					messageBox(this,cnum.getText()+"는 사용 가능");
				}
				else {
					messageBox(this,cnum.getText()+"는 중복");
					
					cnum.setText(""); //코드에 들어간 내용 지우고
					cnum.requestFocus();  //커서 여기에 놓기
				}
			}
		}
	}
	
	public static void messageBox(Object obj, String message) {
		JOptionPane.showMessageDialog( (Component)obj , message);
	}

}
