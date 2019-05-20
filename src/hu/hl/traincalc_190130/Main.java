package hu.hl.traincalc_190130;								
								
import java.awt.Color;								
import java.awt.event.ActionEvent;								
import java.awt.event.ActionListener;								
import java.util.HashMap;								
import java.util.HashSet;								
import java.util.Iterator;								
import java.util.TreeMap;								
import java.util.TreeSet;								
import java.util.Map.Entry;								
import java.util.NavigableSet;								
								
import javax.swing.BorderFactory;								
import javax.swing.DefaultComboBoxModel;								
import javax.swing.JComboBox;								
import javax.swing.JComponent;								
import javax.swing.JFrame;								
import javax.swing.JLabel;								
import javax.swing.JPanel;								
import javax.swing.JSpinner;								
import javax.swing.JTextArea;								
import javax.swing.JTextField;								
import javax.swing.SpinnerNumberModel;								
import javax.swing.border.Border;								
import javax.swing.event.ChangeEvent;								
import javax.swing.event.ChangeListener;								
								
public class Main implements TrainCalcFrameListener1 {								
	private TreeMap<Matter, Wagonz> allwagonz;							
	public static void main(String[] args) {							
		new Main();						
	}							
	public Main() {							
		allwagonz= new TreeMap<Matter, Wagonz>();						
		allwagonz.put(Matter.Uzemanyag, new Wagonz());
		allwagonz.get(Matter.Uzemanyag).put(  0,  6);
		allwagonz.get(Matter.Uzemanyag).put( 10,  2);
		allwagonz.get(Matter.Uzemanyag).put( 20,  6);
		allwagonz.get(Matter.Uzemanyag).put( 40,  6);
		allwagonz.get(Matter.Uzemanyag).put( 60, 21);
		allwagonz.get(Matter.Uzemanyag).put( 70,  6);
		allwagonz.get(Matter.Uzemanyag).put( 75,  5);
		allwagonz.get(Matter.Uzemanyag).put( 90,  3);
		allwagonz.get(Matter.Uzemanyag).put(110,  3);
		allwagonz.get(Matter.Uzemanyag).put(120,  3);
		allwagonz.get(Matter.Uzemanyag).put(280,  9);
		allwagonz.get(Matter.Uzemanyag).put(300,  3);
		allwagonz.get(Matter.Uzemanyag).put(330, 10);
		allwagonz.get(Matter.Uzemanyag).put(350, 18);
		allwagonz.get(Matter.Uzemanyag).put(450,  14);
		allwagonz.get(Matter.Uzemanyag).put(480, 20);
		allwagonz.get(Matter.Uzemanyag).put(500,  13);
		allwagonz.put(Matter.Muanyag, new Wagonz());
		allwagonz.get(Matter.Muanyag).put(  0,  1);
		allwagonz.get(Matter.Muanyag).put( 20,  4);
		allwagonz.get(Matter.Muanyag).put( 40,  1);
		allwagonz.get(Matter.Muanyag).put( 45, 43);
		allwagonz.get(Matter.Muanyag).put( 45,  7);
		allwagonz.get(Matter.Muanyag).put(110,  1);
		allwagonz.get(Matter.Muanyag).put(130,  4);
		allwagonz.get(Matter.Muanyag).put(130,  5);
		allwagonz.get(Matter.Muanyag).put(135, 13);
		allwagonz.get(Matter.Muanyag).put(275,  1);
		allwagonz.get(Matter.Muanyag).put(350,  2);
		allwagonz.get(Matter.Muanyag).put(350, 31);
		allwagonz.get(Matter.Muanyag).put(400, 25);
		allwagonz.get(Matter.Muanyag).put(500,  5);
		allwagonz.get(Matter.Muanyag).put(500,  1);
		allwagonz.get(Matter.Muanyag).put(500,  1);
		allwagonz.get(Matter.Muanyag).put(800,  4);		
		new TrainCalcFrame(allwagonz.navigableKeySet()).setListener1(this);						
	}							
	public TrainCouple change(Coefficients coefficients, boolean parallel, int period, Matter matter, int quantity, int maxwagoncount) {							
		TrainCouples traincouples= new TrainCouples(allwagonz.get(matter), quantity, coefficients, 1, maxwagoncount, coefficients, 1, maxwagoncount, period, parallel);						
		return (traincouples.isEmpty()) ? null : traincouples.first();						
	}							
}								
								
enum Matter {Fa ("Fa"), Szog ("Szög"), Tegla ("Tégla"), Uveg ("Üveg"), Uzemanyag ("Üzemanyag"), Acel ("Acél"), Kavics ("Kavics"), Uran ("Urán"), Cement ("Cement"), Gumi ("Gumi"), Karbon ("Karbon"), Titan ("Titán"), Marvany ("Márvány"), Kabel ("Kábel"), Muanyag ("Műanyag"), Szilicium("Szilicium"), Litium ("Lítium"), Neodimium ("Neodímium"), Volfram ("Volfrám"), Xenon("Xenon"), Bizmut ("Bizmut");								
	private String name;							
	Matter(String name) {							
		this.name= name;						
	}							
	public String getName() {							
		return name;						
	}							
}								
								
class Coefficients {								
	public static enum BuildingCoefficient {N (1.0f), Y (1.5f);							
		private final float coefficient;						
		BuildingCoefficient(float coefficient) {						
			this.coefficient= coefficient;					
		}						
		public float getCoefficient() {						
			return coefficient;					
		}						
	}							
	public static enum LandingCoefficient {N (1.0f), Y (1.1f);							
		private final float coefficient;						
		LandingCoefficient(float coefficient) {						
			this.coefficient= coefficient;					
		}						
		public float getCoefficient() {						
			return coefficient;					
		}						
	}							
	public static enum TrainType {G (1.0f), D (1.4f*1.33f), V (1.0f), M (1.0f), H (1.0f);							
		private final float coefficient;						
		TrainType(float coefficient) {						
			this.coefficient= coefficient;					
		}						
		public float getCoefficient() {						
			return coefficient;					
		}						
	}							
	/** Azok a százalékok, amelyek ismeretlen okból, vagononként 1-gyel többet visznek. */							
	private static HashMap<TrainType, HashSet<Integer>> irregularpercents= new HashMap<TrainType, HashSet<Integer>>();							
	static {							
		irregularpercents.put(TrainType.D, new HashSet<Integer>());						
		irregularpercents.get(TrainType.D).add(65);						
		irregularpercents.get(TrainType.D).add(285);						
		irregularpercents.get(TrainType.D).add(630);						
		irregularpercents.put(TrainType.G, new HashSet<Integer>());						
		irregularpercents.get(TrainType.G).add(125);						
		irregularpercents.get(TrainType.G).add(285);						
		irregularpercents.get(TrainType.G).add(300);						
		irregularpercents.get(TrainType.G).add(340);						
		irregularpercents.put(TrainType.V, irregularpercents.get(TrainType.G));						
		irregularpercents.put(TrainType.M, irregularpercents.get(TrainType.G));						
		irregularpercents.put(TrainType.H, irregularpercents.get(TrainType.G));						
	}							
	private static int getIrregularPercentDifference(TrainType traintype, int percent) {							
		return (irregularpercents.get(traintype).contains(percent)) ? 1 : 0;						
	}							
								
	private BuildingCoefficient buildingcoefficient;							
	private LandingCoefficient landingcoefficient;							
	private TrainType traintype;							
	public Coefficients(BuildingCoefficient buildingcoefficient, LandingCoefficient landingcoefficient, TrainType traintype) {							
		this.buildingcoefficient= buildingcoefficient;						
		this.landingcoefficient= landingcoefficient;						
		this.traintype= traintype;						
	}							
	/** A megadott százalék egy vagonja által elszállított mennyiség */							
	public int getPercentSum(int percent) {							
		return (int) Math.ceil((float) Math.ceil((100+percent)*buildingcoefficient.getCoefficient()*traintype.getCoefficient()*225/100)*landingcoefficient.getCoefficient()+getIrregularPercentDifference(traintype, percent));						
	}							
	public TrainType getTrainType() {							
		return traintype;						
	}							
	public String toString() {							
		return buildingcoefficient.toString()+"\t"+landingcoefficient.toString()+"\t"+traintype.toString();						
	}							
}								
								
class Wagonz {								
	private final TreeMap<Integer, Integer> countlist= new TreeMap<Integer, Integer>();							
	private final TreeMap<Integer, Integer> indexlist= new TreeMap<Integer, Integer>();							
	public void put(int percent, int count) {							
		countlist.put(percent, ((countlist.containsKey(percent)) ? countlist.get(percent) : 0)+count);
		indexlist.clear();						
		Iterator<Entry<Integer, Integer>> ieinteger= countlist.entrySet().iterator();						
		while (ieinteger.hasNext()) {						
			Entry<Integer, Integer> einteger= ieinteger.next();					
			indexlist.put((indexlist.isEmpty()) ? 0 : indexlist.lastKey(), einteger.getKey());					
			indexlist.put(indexlist.lastKey()+einteger.getValue(), null);					
		}						
	}							
	/** A megadott wagonz elemeit kivonja ebből a wagonzból, és az új wagonzba rakott maradékkal tér vissza. */							
	public Wagonz getDifference(Wagonz wagonz) {							
		Wagonz result= new Wagonz();						
		Iterator<Entry<Integer, Integer>> ieinteger= countlist.entrySet().iterator();						
		while (ieinteger.hasNext()) {						
			Entry<Integer, Integer> einteger= ieinteger.next();					
			int count= einteger.getValue()-((wagonz.countlist.containsKey(einteger.getKey())) ? wagonz.countlist.get(einteger.getKey()) : 0);					
			if (0<count) {					
				result.put(einteger.getKey(), count);				
			}					
		}						
		return result;						
	}							
	/** A megadott indexen levő százalék. */							
	public int getPercent(int index) {							
		return indexlist.floorEntry(index).getValue();						
	}							
	/** A megadott indexen levő százalékú wagonok száma az indexig. */							
	public int getPercentCount(int index) {							
		return index-indexlist.floorKey(index)+1;						
	}							
	public int getTotalCount() {							
		return (indexlist.isEmpty()) ? 0 : indexlist.lastKey();						
	}							
	public int getTotalSum(Coefficients coefficients) {							
		int result= 0;						
		Iterator<Entry<Integer, Integer>> iepercent= countlist.entrySet().iterator();						
		while (iepercent.hasNext()) {						
			Entry<Integer, Integer> einteger= iepercent.next();					
			result+= coefficients.getPercentSum(einteger.getKey())*einteger.getValue();					
		}						
		return result;						
	}							
	/** A megadott darabszámú vagon által elszállított minimális összeg.							
	* @return -1= több vagon van megadva, mint az összes */							
	public int getMinSum(Coefficients coefficients, int count) {							
		int result= (getTotalCount()<count) ? -1 : 0;						
		int index= 0;						
		while (-1<result && index<count) {						
			result+= coefficients.getPercentSum(getPercent(index));					
			index++;					
		}						
		return result;						
	}							
	/** A megadott darabszámú vagon által elszállított maximális összeg.							
	* @return -1= több vagon van megadva, mint az összes */							
	public int getMaxSum(Coefficients coefficients, int count) {							
		int result= (getTotalCount()<count) ? -1 : 0;						
		int index= (indexlist.isEmpty()) ? 0 : indexlist.lastKey();						
		while (-1<result && indexlist.lastKey()-count<index) {						
			index--;					
			result+= coefficients.getPercentSum(getPercent(index));					
		}						
		return result;						
	}							
	public String toString() {							
		return countlist.toString();						
	}							
}								
								
class Train {								
	private final Coefficients coefficients;							
	private final Wagonz wagonz;							
	private final int period;							
	public Train(Coefficients coefficients, Wagonz wagonz, int count, int sum, int period) {							
		this.coefficients= coefficients;						
		this.period= period;						
		this.wagonz= (wagonz.getMinSum(coefficients, count)<=sum && sum<=wagonz.getMaxSum(coefficients, count)) ? new Wagonz() : null;						
		while (this.wagonz!=null && 0<count) {						
			int first= 0;					
			int last= 0;					
			int sum0= 0;					
			while (sum0<sum) {					
				sum0+= coefficients.getPercentSum(wagonz.getPercent(first));				
				if (count<first-last+1) {				
					sum0-= coefficients.getPercentSum(wagonz.getPercent(last));			
					last++;			
				}				
				if (sum0<sum) {				
					first++;			
				}				
			}					
			count-= wagonz.getPercentCount(first);					
			sum-= coefficients.getPercentSum(wagonz.getPercent(first))*wagonz.getPercentCount(first);					
			this.wagonz.put(wagonz.getPercent(first), wagonz.getPercentCount(first));					
		}						
	}							
	public boolean isValid() {							
		return wagonz!=null;						
	}							
	public Wagonz getWagonz() {							
		return wagonz;						
	}							
	public int getPeriod() {							
		return period;						
	}							
	public int getOnePeriodSum() {							
		return wagonz.getTotalSum(coefficients);						
	}							
	public String toString() {							
		return /*period+" "+*/wagonz.getTotalSum(coefficients)+" "+coefficients.getTrainType()+" "+wagonz.getTotalCount()+" "+wagonz.toString();						
	}							
}								
								
class TrainCouple implements Comparable<TrainCouple> {								
	public final Train train1;							
	public final Train trainn;							
	public TrainCouple(Train train1, Train trainn) {							
		this.train1= train1;						
		this.trainn= trainn;						
	}							
	public int compareTo(TrainCouple traincouple) {							
		return trainn.getPeriod()-traincouple.trainn.getPeriod();						
	}							
	public String toString() {							
		return train1.toString()+((0<trainn.getPeriod()) ? "\r\n"+trainn.toString() : "");						
	}							
}								
								
class TrainCouples extends TreeSet<TrainCouple> {								
	private static final long serialVersionUID = 1L;							
	/**							
	*							
	* @param wagonz Az adott anyagból rendelkezésre álló összes vagon.							
	* @param sum Elszállítandó mennyiség.							
	* @param coefficients1 Az egyszer közlekedő vonathoz együtthatók (épület, vonattipus, kirakás).							
	* @param mincount1 Az egyszer közlekedő vonat vagonjainak min. száma.							
	* @param maxcount1 Az egyszer közlekedő vonat vagonjainak max. száma.							
	* @param coefficientsn A többször közlekedő vonathoz együtthatók (épület, vonattipus, kirakás).							
	* @param mincountn A többször közlekedő vonat vagonjainak min. száma.							
	* @param maxcountn A többször közlekedő vonat vagonjainak max. száma.							
	* @param minperiod Körök min. száma.							
	* @param maxperiod Körök max. száma.							
	* @param parallel True esetén a két vonat egyszerre sem használ fel az összesnél több vagont, így egymás zavarása nélkül, egyidőben összeállíthatóak.							
	*/							
	public TrainCouples(Wagonz wagonz, int sum, Coefficients coefficients1, int mincount1, int maxcount1, Coefficients coefficientsn, int mincountn, int maxcountn, int minperiod, int maxperiod, boolean parallel) {							
		int period= minperiod;						
		while (period<=maxperiod) {						
			int countn= mincountn;					
			while (countn<=maxcountn) {					
				Train trainn= new Train(coefficientsn, wagonz, countn, (int) Math.ceil((float) sum/period), period-1);				
				if (trainn.isValid()) {				
					int count1= mincount1;			
					while (count1<=maxcount1) {			
						Train train1= new Train(coefficients1, (parallel && 0<trainn.getPeriod()) ? wagonz.getDifference(trainn.getWagonz()) : wagonz, count1, sum-trainn.getOnePeriodSum()*trainn.getPeriod(), 1);		
						if (train1.isValid() && train1.getOnePeriodSum()==sum-trainn.getOnePeriodSum()*trainn.getPeriod()) {		
							add(new TrainCouple(train1, trainn));	
						}		
						count1++;		
					}			
				}				
				countn++;				
			}					
			period++;					
		}						
	}							
	public TrainCouples(Wagonz wagonz, int sum, Coefficients coefficients1, int mincount1, int maxcount1, Coefficients coefficientsn, int mincountn, int maxcountn, int period, boolean parallel) {							
		int countn= mincountn;						
		while (countn<=maxcountn) {						
			Train trainn= new Train(coefficientsn, wagonz, countn, (int) Math.ceil((float) sum/period), period-1);					
			if (trainn.isValid()) {					
				int count1= mincount1;				
				while (count1<=maxcount1) {				
					Train train1= new Train(coefficients1, (parallel && 0<trainn.getPeriod()) ? wagonz.getDifference(trainn.getWagonz()) : wagonz, count1, sum-trainn.getOnePeriodSum()*trainn.getPeriod(), 1);			
					if (train1.isValid() && train1.getOnePeriodSum()==sum-trainn.getOnePeriodSum()*trainn.getPeriod()) {			
						add(new TrainCouple(train1, trainn));		
					}			
					count1++;			
				}				
			}					
			countn++;					
		}						
	}							
	public String toString() {							
		String result= "";						
		Iterator<TrainCouple> itc= iterator();						
		while (itc.hasNext()) {						
			result+= itc.next()+((itc.hasNext()) ? "\r\n\r\n" : "");					
		}						
		return result;						
	}							
}								
								
class TrainCalcFrame implements MatterPanelListener1 {								
	private JFrame frame= new JFrame();							
	public static Border border= BorderFactory.createLineBorder(Color.black);							
	private HeaderPanel headerpanel;							
	private MatterPanel matterpanel;							
	private TrainCalcFrameListener1 listener1;							
	public TrainCalcFrame(NavigableSet<Matter> matters) {							
		headerpanel= new HeaderPanel();						
		matterpanel= new MatterPanel(matters);						
		frame.setTitle("TrainCalc");						
		frame.setLayout(null);						
		frame.setBounds(0, 0, 352, 413);						
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);						
		headerpanel.setLocation(0, 0);						
		headerpanel.setVisible(true);						
		matterpanel.setLocation(0, (headerpanel.getLocation().y+headerpanel.getSize().height));						
		matterpanel.setVisible(true);						
		frame.add(headerpanel);						
		frame.add(matterpanel);						
		frame.setVisible(true);						
		headerpanel.setListener1(matterpanel);						
		matterpanel.setListener1(this);						
		matterpanel.setListener2(headerpanel);						
	}							
	public void setListener1(TrainCalcFrameListener1 listener1) {							
		this.listener1= listener1;						
		headerpanel.change0();						
	}							
	public TrainCouple change(Coefficients coefficients, boolean parallel, int period, Matter matter, int quantity, int maxwagoncount) {							
		return listener1.change(coefficients, parallel, period, matter, quantity, maxwagoncount);						
	}							
}								
								
interface TrainCalcFrameListener1 extends MatterPanelListener1 {								
}								
								
enum Bool {N (false), Y (true);								
	private final boolean value;							
	Bool(boolean value) {							
		this.value= value;						
	}							
	public boolean getValue() {							
		return value;						
	}							
}								
								
class HeaderPanel extends JPanel implements ActionListener, ChangeListener, MatterPanelListener2 {								
	private static final long serialVersionUID = 1L;							
	private HeaderPanelListener1 listener1;							
	private JLabel lbuilding= new JLabel();							
	private JLabel llanding= new JLabel();							
	private JLabel ltraintype= new JLabel();							
	private JLabel lparallel= new JLabel();							
	private JLabel lperiod= new JLabel();							
	private JLabel ltotalreswagon= new JLabel();							
	private JComboBox<Coefficients.BuildingCoefficient> vbuilding= new JComboBox<Coefficients.BuildingCoefficient>();							
	private JComboBox<Coefficients.LandingCoefficient> vlanding= new JComboBox<Coefficients.LandingCoefficient>();							
	private JComboBox<Coefficients.TrainType> vtraintype= new JComboBox<Coefficients.TrainType>();							
	private JComboBox<Bool> vparallel= new JComboBox<Bool>();							
	private JSpinner vperiod= new JSpinner();							
	private JLabel vtotalreswagon= new JLabel();							
	public HeaderPanel() {							
		lbuilding.setText("Épület:");						
		llanding.setText("Kirakodás:");						
		ltraintype.setText("Mozd. tip.:");						
		lparallel.setText("Párh.:");						
		lperiod.setText("Kör:");						
		ltotalreswagon.setText("Össz. vagon:");						
		vbuilding.setModel(new DefaultComboBoxModel<Coefficients.BuildingCoefficient>(Coefficients.BuildingCoefficient.values()));						
		vbuilding.setSelectedItem(Coefficients.BuildingCoefficient.Y);						
		vlanding.setModel(new DefaultComboBoxModel<Coefficients.LandingCoefficient>(Coefficients.LandingCoefficient.values()));						
		vlanding.setSelectedItem(Coefficients.LandingCoefficient.Y);						
		vtraintype.setModel(new DefaultComboBoxModel<Coefficients.TrainType>(Coefficients.TrainType.values()));						
		vtraintype.setSelectedItem(Coefficients.TrainType.D);						
		vparallel.setModel(new DefaultComboBoxModel<Bool>(Bool.values()));						
		vperiod.setModel(new SpinnerNumberModel(2, 2, 999, 1));						
		vtotalreswagon.setText("");						
		lbuilding.setBounds(2, 2, 80, 20);						
		llanding.setBounds(2, 26, 80, 20);						
		ltraintype.setBounds(2, 50, 80, 20);						
		lparallel.setBounds(170, 2, 80, 20);						
		lperiod.setBounds(170, 26, 80, 20);						
		ltotalreswagon.setBounds(170, 50, 80, 20);						
		vbuilding.setBounds(86, 2, 80, 20);						
		vlanding.setBounds(86, 26, 80, 20);						
		vtraintype.setBounds(86, 50, 80, 20);						
		vparallel.setBounds(254, 2, 80, 20);						
		vperiod.setBounds(254, 26, 80, 20);						
		vtotalreswagon.setBounds(254, 50, 80, 20);						
		lbuilding.setBorder(TrainCalcFrame.border);						
		llanding.setBorder(TrainCalcFrame.border);						
		ltraintype.setBorder(TrainCalcFrame.border);						
		lparallel.setBorder(TrainCalcFrame.border);						
		lperiod.setBorder(TrainCalcFrame.border);						
		ltotalreswagon.setBorder(TrainCalcFrame.border);						
		vbuilding.setBorder(TrainCalcFrame.border);						
		vlanding.setBorder(TrainCalcFrame.border);						
		vtraintype.setBorder(TrainCalcFrame.border);						
		vparallel.setBorder(TrainCalcFrame.border);						
		vperiod.setBorder(TrainCalcFrame.border);						
		vtotalreswagon.setBorder(TrainCalcFrame.border);						
		lbuilding.setOpaque(false);						
		llanding.setOpaque(false);						
		ltraintype.setOpaque(false);						
		lparallel.setOpaque(false);						
		lperiod.setOpaque(false);						
		ltotalreswagon.setOpaque(false);						
		vbuilding.setOpaque(false);						
		vlanding.setOpaque(false);						
		vtraintype.setOpaque(false);						
		vparallel.setOpaque(false);						
		vperiod.setOpaque(false);						
		vperiod.getEditor().setOpaque(false);						
		((JSpinner.DefaultEditor) vperiod.getEditor()).getTextField().setOpaque(false);						
		vtotalreswagon.setOpaque(false);						
		lbuilding.setHorizontalAlignment(JTextField.LEFT);						
		llanding.setHorizontalAlignment(JTextField.LEFT);						
		ltraintype.setHorizontalAlignment(JTextField.LEFT);						
		lparallel.setHorizontalAlignment(JTextField.LEFT);						
		lperiod.setHorizontalAlignment(JTextField.LEFT);						
		ltotalreswagon.setHorizontalAlignment(JTextField.LEFT);						
		lbuilding.setVisible(true);						
		llanding.setVisible(true);						
		ltraintype.setVisible(true);						
		lparallel.setVisible(true);						
		lperiod.setVisible(true);						
		ltotalreswagon.setVisible(true);						
		vbuilding.setVisible(true);						
		vlanding.setVisible(true);						
		vtraintype.setVisible(true);						
		vparallel.setVisible(true);						
		vperiod.setVisible(true);						
		vtotalreswagon.setVisible(true);						
		vbuilding.addActionListener(this);						
		vlanding.addActionListener(this);						
		vtraintype.addActionListener(this);						
		vparallel.addActionListener(this);						
		vperiod.addChangeListener(this);						
		add(lbuilding);						
		add(llanding);						
		add(ltraintype);						
		add(lparallel);						
		add(lperiod);						
		add(ltotalreswagon);						
		add(vbuilding);						
		add(vlanding);						
		add(vtraintype);						
		add(vparallel);						
		add(vperiod);						
		add(vtotalreswagon);						
		setLayout(null);						
		setSize(336, 72);						
	}							
	public void setListener1(HeaderPanelListener1 listener1) {							
		this.listener1= listener1;						
	}							
	public void stateChanged(ChangeEvent changeevent) {							
		listener1.change(new Coefficients(vbuilding.getItemAt(vbuilding.getSelectedIndex()), vlanding.getItemAt(vlanding.getSelectedIndex()), vtraintype.getItemAt(vtraintype.getSelectedIndex())), ((Bool) vparallel.getItemAt(vparallel.getSelectedIndex())).getValue(), (Integer) vperiod.getValue());						
	}							
	public void actionPerformed(ActionEvent actionevent) {							
		listener1.change(new Coefficients(vbuilding.getItemAt(vbuilding.getSelectedIndex()), vlanding.getItemAt(vlanding.getSelectedIndex()), vtraintype.getItemAt(vtraintype.getSelectedIndex())), ((Bool) vparallel.getItemAt(vparallel.getSelectedIndex())).getValue(), (Integer) vperiod.getValue());						
	}							
	public void change0() {							
		listener1.change(new Coefficients(vbuilding.getItemAt(vbuilding.getSelectedIndex()), vlanding.getItemAt(vlanding.getSelectedIndex()), vtraintype.getItemAt(vtraintype.getSelectedIndex())), ((Bool) vparallel.getItemAt(vparallel.getSelectedIndex())).getValue(), (Integer) vperiod.getValue());						
	}							
	public void change1(int totalwagoncount) {							
		vtotalreswagon.setText((0<totalwagoncount) ? String.valueOf(totalwagoncount) : "");						
	}							
}								
								
interface HeaderPanelListener1 {								
	public void change(Coefficients coefficients, boolean parallel, int period);							
}								
								
class MatterPanel extends JPanel implements ChangeListener, HeaderPanelListener1 {								
	private static final long serialVersionUID = 1L;							
	private MatterPanelListener1 listener1;							
	private MatterPanelListener2 listener2;							
	private JLabel lmattername= new JLabel();							
	private JLabel lquantity= new JLabel();							
	private JLabel lmaxwagon= new JLabel();							
	private JLabel lreswagon= new JLabel();							
	private NavigableSet<Matter> matters;							
	private HashMap<JComponent, Matter> components= new HashMap<JComponent, Matter>();							
	private TreeMap<Matter, JSpinner> vquantities= new TreeMap<Matter, JSpinner>();							
	private TreeMap<Matter, JSpinner> vmaxwagons= new TreeMap<Matter, JSpinner>();							
	private TreeMap<Matter, JLabel> vreswagons= new TreeMap<Matter, JLabel>();							
	private TreeMap<Matter, TrainCouple> restraincouples= new TreeMap<Matter, TrainCouple>();							
	private JTextArea vrestrains= new JTextArea();							
	public MatterPanel(NavigableSet<Matter> matters) {							
		this.matters= matters;						
		lmattername.setText("Anyagnév");						
		lquantity.setText("Mennyiség");						
		lmaxwagon.setText("Max. vagon");						
		lreswagon.setText("Vagon");						
		lmattername.setBounds(2, 2, 80, 20);						
		lquantity.setBounds(86, 2, 80, 20);						
		lmaxwagon.setBounds(170, 2, 80, 20);						
		lreswagon.setBounds(254, 2, 80, 20);						
		lmattername.setBorder(TrainCalcFrame.border);						
		lquantity.setBorder(TrainCalcFrame.border);						
		lmaxwagon.setBorder(TrainCalcFrame.border);						
		lreswagon.setBorder(TrainCalcFrame.border);						
		lmattername.setOpaque(false);						
		lquantity.setOpaque(false);						
		lmaxwagon.setOpaque(false);						
		lreswagon.setOpaque(false);						
		lmattername.setHorizontalAlignment(JTextField.CENTER);						
		lquantity.setHorizontalAlignment(JTextField.CENTER);						
		lmaxwagon.setHorizontalAlignment(JTextField.CENTER);						
		lreswagon.setHorizontalAlignment(JTextField.CENTER);						
		lmattername.setVisible(true);						
		lquantity.setVisible(true);						
		lmaxwagon.setVisible(true);						
		lreswagon.setVisible(true);						
		add(lmattername);						
		add(lquantity);						
		add(lmaxwagon);						
		add(lreswagon);						
		Iterator<Matter> imatter= matters.iterator();						
		while (imatter.hasNext()) {						
			Matter matter= imatter.next();					
			JLabel vmattername= new JLabel();					
			JSpinner vquantity= new JSpinner();					
			JSpinner vmaxwagon= new JSpinner();					
			JLabel vreswagon= new JLabel();					
			this.components.put(vquantity, matter);					
			this.components.put(vmaxwagon, matter);					
			vquantities.put(matter, vquantity);					
			vmaxwagons.put(matter, vmaxwagon);					
			vreswagons.put(matter, vreswagon);					
			vmattername.setText(matter.getName());					
			vquantity.setModel(new SpinnerNumberModel(100000, 1, 99999999, 1));					
			vmaxwagon.setModel(new SpinnerNumberModel(29, 1, 999, 1));					
			vmattername.setBounds(2, vquantities.size()*24+2, 80, 20);					
			vquantity.setBounds(86, vquantities.size()*24+2, 80, 20);					
			vmaxwagon.setBounds(170, vquantities.size()*24+2, 80, 20);					
			vreswagon.setBounds(254, vquantities.size()*24+2, 80, 20);					
			vmattername.setBorder(TrainCalcFrame.border);					
			vquantity.setBorder(TrainCalcFrame.border);					
			vmaxwagon.setBorder(TrainCalcFrame.border);					
			vreswagon.setBorder(TrainCalcFrame.border);					
			vmattername.setOpaque(false);					
			vquantity.setOpaque(false);					
			vquantity.getEditor().setOpaque(false);					
			((JSpinner.DefaultEditor) vquantity.getEditor()).getTextField().setOpaque(false);					
			vmaxwagon.setOpaque(false);					
			vmaxwagon.getEditor().setOpaque(false);					
			vreswagon.setOpaque(false);					
			((JSpinner.DefaultEditor) vmaxwagon.getEditor()).getTextField().setOpaque(false);					
			vmattername.setHorizontalAlignment(JTextField.CENTER);					
			vreswagon.setHorizontalAlignment(JTextField.RIGHT);					
			vmattername.setVisible(true);					
			vquantity.setVisible(true);					
			vmaxwagon.setVisible(true);					
			vreswagon.setVisible(true);					
			vquantity.addChangeListener(this);					
			vmaxwagon.addChangeListener(this);					
			add(vmattername);					
			add(vquantity);					
			add(vmaxwagon);					
			add(vreswagon);					
		}						
		vrestrains.setBounds(2, (vquantities.size()+1)*24+2, vreswagons.entrySet().iterator().next().getValue().getLocation().x+vreswagons.entrySet().iterator().next().getValue().getSize().width-2, 178);						
		vrestrains.setBorder(TrainCalcFrame.border);						
		vrestrains.setOpaque(true);						
		vrestrains.setVisible(true);						
		vrestrains.setEditable(false);						
		add(vrestrains);						
		setLayout(null);						
		setSize(336, vrestrains.getLocation().y+vrestrains.getSize().height+2);						
	}							
	public void setListener1(MatterPanelListener1 listener1) {							
		this.listener1= listener1;						
	}							
	public void setListener2(MatterPanelListener2 listener2) {							
		this.listener2= listener2;						
	}							
	public void stateChanged(ChangeEvent changeevent) {							
		listener2.change0();						
	}							
	public void change(Coefficients coefficients, boolean parallel, int period) {							
		Iterator<Matter> imatter= matters.iterator();						
		int totalwagoncount= 0;						
		while (imatter.hasNext()) {						
			Matter matter= imatter.next();					
			vreswagons.get(matter).setText("");					
			TrainCouple traincouple= listener1.change(coefficients, parallel, period, matter, (Integer) vquantities.get(matter).getValue(), (Integer) vmaxwagons.get(matter).getValue());					
			restraincouples.put(matter, traincouple);					
			totalwagoncount= (totalwagoncount==-1 || traincouple==null) ? -1 : totalwagoncount+Math.max(traincouple.train1.getWagonz().getTotalCount(), traincouple.trainn.getWagonz().getTotalCount());					
		}						
		String table= "";						
		int i= 0;						
		boolean end= false;						
		while (!end) {						
			Iterator<Entry<Matter, TrainCouple>> ierestraincouple= restraincouples.entrySet().iterator();					
			while (ierestraincouple.hasNext()) {					
				Entry<Matter, TrainCouple> erestraincouple= ierestraincouple.next();				
				switch (i) {				
				case 0:				
					table+= erestraincouple.getKey().getName()+((ierestraincouple.hasNext()) ? "\t" : "");			
					break;			
				case 1:				
					table+= ((erestraincouple.getValue()==null) ? "" : erestraincouple.getValue().train1.toString())+((ierestraincouple.hasNext()) ? "\t" : "");			
					if (erestraincouple.getValue()!=null) {			
						int reswagon1= erestraincouple.getValue().train1.getWagonz().getTotalCount();		
						int reswagonn= erestraincouple.getValue().trainn.getWagonz().getTotalCount();		
						vreswagons.get(erestraincouple.getKey()).setText(String.valueOf(Math.max(reswagon1, reswagonn)));		
					}			
					break;			
				default:				
					table+= ((erestraincouple.getValue()==null || erestraincouple.getValue().trainn.getPeriod()<i-1) ? "" : erestraincouple.getValue().trainn.toString())+((ierestraincouple.hasNext()) ? "\t" : "");			
					break;			
				}				
				end= true;				
				if (erestraincouple.getValue()!=null) {				
					end= end && i==erestraincouple.getValue().trainn.getPeriod()+1;			
				}				
			}					
			i++;					
			table+= (!end) ? "\r\n" : "";					
		}						
		vrestrains.setText(table);						
		listener2.change1(totalwagoncount);						
	}							
}								
								
interface MatterPanelListener1 {								
	public TrainCouple change(Coefficients coefficients, boolean parallel, int period, Matter matter, int quantity, int maxwagoncount);							
}								
								
interface MatterPanelListener2 {								
	public void change0();							
	public void change1(int totalwagoncount);							
}								