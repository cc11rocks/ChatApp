// The IP and port of the server was replaced with asterics. Ideally, this would be specified in a configuration file.

import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.*; //getTime() 00:00 ...look at when they are BOTH 0's
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*; //Make mod GUI
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.sound.sampled.*;
import java.util.jar.*;
import javax.net.ssl.*;

public class AppletClient extends JApplet {
private JTextArea typingUsers = new JTextArea("");
private JTextArea modLog = new JTextArea("Mod Log...");
private JButton submitChange = new JButton("Change Password");
private JPasswordField newPass = new JPasswordField("");
private JPasswordField oldPass = new JPasswordField("");
private JTextField changePassName = new JTextField("");
private JButton deleteAccount = new JButton("Delete Account");
private JTextArea accountLog = new JTextArea("Account Details log...");
private JPasswordField delPFPass = new JPasswordField("");
private JTextField delPFUser = new JTextField("");
private JButton loginButton = new JButton("Login");
private JButton submit = new JButton("Register");
private JButton login = new JButton("Login");
private JTextField rname = new JTextField();
private JTextField lname = new JTextField();
private JPasswordField rpass = new JPasswordField();
private JPasswordField lpass = new JPasswordField();
private JTextField rmail = new JTextField();
private JTextArea log = new JTextArea("Register/Login log...");
private DefaultListModel list = new DefaultListModel();
private JList onlineUsers;
private JButton black = new JButton("Black");
private JButton darkGreen = new JButton("Dark Green");
private JButton medSeaGreen = new JButton("Medium Sea Green");
private JButton darkPink = new JButton("Dark Pink");
private JButton lightPink = new JButton("Light Pink");
private JButton cyan = new JButton("Cyan");
private JButton purple = new JButton("Purple");
private JButton yellow = new JButton("Yellow");
private JPanel color = new JPanel();
private static final String string_fq = "You can easily send a Private Message by clicking on the username in the top right hand section of the main screen you want to send your message to. If I wanted" +
" to message Billy and tell him that his pet dog was running across the street, I would click on his name in the main screen user list and" +
" type \"Your dog, Bob, is running across the street. I'll get him for you!\"";
private double soundD = 0.5d;
private JTabbedPane mainPane = new JTabbedPane();
private Clip clip1;
private String myTempName;
//Socket sock;
private SSLSocket sock;
private BufferedReader reader;
private PrintWriter writer;
private JTextField outgoing = new JTextField();
private JTextField name = new JTextField();
private StyledDocument doc = new DefaultStyledDocument();
private SimpleAttributeSet attributes = new SimpleAttributeSet();
private JTextPane no = new JTextPane(doc);
private JScrollPane scroll = new JScrollPane(no, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//private JScrollPane usersTyping = new JScrollPane(typingUsers, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
private Timer timer;
private Point pnt;

@SuppressWarnings("unchecked") 
public static void main (String[] args) {
System.getSecurityManager().checkPermission(new SocketPermission("***.***.***.***", "accept, connect, listen"));
System.getSecurityManager().checkPermission(new RuntimePermission("readerThread"));
new AppletClient();
}
public void init() {
timer = new Timer(1000, new ActionListener() {
	public void actionPerformed(ActionEvent userTyping) {
		timer.stop();
		writethis("NOTCURRENTLYTYPING");
	}
});
onlineUsers = new JList(list);
onlineUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
onlineUsers.addMouseListener(new MouseListener() {
public void mouseEntered(MouseEvent bs) {}
public void mouseExited(MouseEvent bsa) {}
public void mousePressed(MouseEvent bsaf) {}
public void mouseReleased(MouseEvent last) {}
public void mouseClicked(MouseEvent af) {
JList theList = (JList) af.getSource();
int index = theList.locationToIndex(af.getPoint());
if (index >= 0) {
	Object o = theList.getModel().getElementAt(index);
	outgoing.setText(o.toString() + "@");
	outgoing.requestFocus();
}
}
});
JScrollPane onlineUserPane = new JScrollPane(onlineUsers);
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
no.setAutoscrolls(true);
no.setEditable(false);
typingUsers.setEditable(false);
//no.setOpaque(false);
scroll.setAutoscrolls(true);
JPanel top = new JPanel();
top.setLayout(new BorderLayout());
JLabel tellItLikeItIs = new JLabel("Top = User Name, Bottom = Message. To send a personal message, click the receiving name in the list to your top right");
top.setBackground(new Color(200, 221, 242));
JPanel scrollPanels = new JPanel();
scrollPanels.setLayout(new BorderLayout());
typingUsers.setBackground(new Color(200, 221, 242));
scrollPanels.add("North", typingUsers); // This line screws it up somehow
//typingUsers.setMinimumSize(new Dimension(100, 40));
scrollPanels.add("Center", scroll);
top.add("Center", scrollPanels);
top.add("East", onlineUserPane);
top.add("South", tellItLikeItIs);
JPanel main = new JPanel();
main.setLayout(new BorderLayout());
main.add("Center", top);
JPanel bottom = new JPanel();
bottom.setLayout(new BorderLayout());
bottom.add("Center", outgoing);
bottom.add("North", name);
main.add("South", bottom);
mainPane.add("Message Board", main);
JPanel soundControls = new JPanel();
soundControls.setLayout(new FlowLayout());
JLabel soundVolumeLabel = new JLabel ("Sound Volume : ");
soundControls.add(soundVolumeLabel);
JSlider volume = new JSlider(0, 9, 5);
volume.setMajorTickSpacing(2);
volume.setMinorTickSpacing(1);
volume.setPaintLabels(true);
volume.setPaintTicks(true);
soundControls.add(volume);
volume.addChangeListener(new ChangeListener() {
public void stateChanged(ChangeEvent e) {
JSlider source = (JSlider)e.getSource();
int soundWanted = (int)source.getValue();
 String aString = Integer.toString(soundWanted);
 aString = "0." + aString;
 soundD = Double.valueOf(aString).doubleValue();
}
});
setButtonAction(darkGreen, "DARKGREEN");
setButtonAction(medSeaGreen, "MEDSEAGREEN");
setButtonAction(black, "BLACK");
setButtonAction(lightPink, "LIGHTPINK");
setButtonAction(darkPink, "DARKPINK");
setButtonAction(purple, "PURPLE");
setButtonAction(cyan, "CYAN");
setButtonAction(yellow, "YELLOW");
black.setBackground(Color.BLACK);
black.setForeground(Color.WHITE);
darkGreen.setBackground(new Color(0, 100, 0));
medSeaGreen.setBackground(new Color(60,179,113));
purple.setBackground(new Color(160,32,240));
lightPink.setBackground(new Color(255,20,147));
darkPink.setBackground(new Color(139,10,80));
yellow.setBackground(Color.YELLOW);
cyan.setBackground(Color.CYAN);
color.add(black);
color.add(darkGreen);
color.add(medSeaGreen);
color.add(purple);
color.add(lightPink);
color.add(darkPink);
color.add(yellow);
color.add(cyan);
JPanel colorSlashSound = new JPanel(new GridLayout(0,1)); // Regular (FlowLayout) for squeeze
colorSlashSound.add(soundControls);
colorSlashSound.add(color);
mainPane.add("Sound & Color", colorSlashSound);
//mainPane.add("Sound Controls", soundControls);
JPanel comingSoon_Login = new JPanel();
comingSoon_Login.setLayout(new GridLayout(0,2));
//JLabel login_ComingSoon = new JLabel("Register -");
JLabel logName = new JLabel("Username (4-11 Characters, Case Sensative) : ");
JLabel logPass = new JLabel("Password (4-11 Characters, Case Sensative) : ");
JLabel regName = new JLabel("Username (4-11 Characters, Case Sensative) : ");
JLabel regPass = new JLabel("Password (4-11 Characters, Case Sensative) : ");
JLabel regMail = new JLabel("Email (10 -50 Characters): ");
addHListener(rname); // Highlight on focus
addHListener(rpass);
addHListener(rmail);
addHListener(lpass);
addHListener(lname);
//JLabel loginStart = new JLabel("Login -");
//comingSoon_Login.add(login_ComingSoon);
//comingSoon_Login.add(fake);
comingSoon_Login.add(regName);
//comingSoon_Login.add(fake);
comingSoon_Login.add(rname);
comingSoon_Login.add(regPass);
comingSoon_Login.add(rpass);
comingSoon_Login.add(regMail);
comingSoon_Login.add(rmail);
comingSoon_Login.add(new JLabel(""));
comingSoon_Login.add(submit);
//comingSoon_Login.add(loginStart);
//comingSoon_Login.add(fake);
comingSoon_Login.add(logName);
comingSoon_Login.add(lname);
comingSoon_Login.add(logPass);
comingSoon_Login.add(lpass);
log.setEditable(false);
log.setLineWrap(true);
log.setWrapStyleWord(true);
JScrollPane logScroller = new JScrollPane(log);
comingSoon_Login.add(logScroller);
comingSoon_Login.add(loginButton);
loginButton.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
boolean part1 = false;
boolean part2 = false;
	if ((lname.getText().length() <= 11) && (lname.getText().length() >= 4)) {
		part1 = true;
	}
	if ((lpass.getPassword().length <=11) && (lpass.getPassword().length >= 4)) {
		part2 = true;
	}
	if (part2 && part1) {
	writethis("LOGINNAME" + lname.getText() + "PASSWORD" + new String(lpass.getPassword()));
	lname.setText("");
	lpass.setText("");
	}
	else if (!part1) {
		log.append("\nYour name is 4-11 characters!");
		log.setCaretPosition(log.getDocument().getLength());
		lname.setText("");
	}
	else if (!part2) {
		log.append("\nYour password is 4-11 characters!");
		log.setCaretPosition(log.getDocument().getLength());
		lpass.setText("");
	}
}
});
/*
loginButton.setEnabled(false);
lpass.setEditable(false);
lname.setEditable(false);
*/
submit.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent aff) {
boolean part1 = false;
boolean part2 = false;
boolean part3 = false;
	if ((rname.getText().length() <= 11) && (rname.getText().length() >= 4)) {
		part1 = true;
	}
	if ((rpass.getPassword().length <=11) && (rpass.getPassword().length >= 4)) {
		part2 = true;
	}
	if ((rmail.getText().length() >=10) && (rmail.getText().length() <= 50)) {
		part3 = true;
	}
	if (part2 && part1 && part3) {
	writethis("REGISTERNAME" + rname.getText() + "PASSWORD" + new String(rpass.getPassword()) + "EMAIL" + rmail.getText());
	rname.setText("");
	rpass.setText("");
	}
	else if (!part1) {
		log.append("\nYour name has to be 4-11 characters!");
		log.setCaretPosition(log.getDocument().getLength());
		rname.setText("");
	}
	else if (!part2) {
		log.append("\nYour password has to be 4-11 characters!");
		log.setCaretPosition(log.getDocument().getLength());
		rpass.setText("");
	}
	else if (!part3) {
		log.append("\nYour email has to be 10-50 characters!");
		log.setCaretPosition(log.getDocument().getLength());
		rmail.setText("");
	}
}
});
JPanel newPanel = new JPanel();
newPanel.setLayout(new BorderLayout());
//newPanel.add(login_ComingSoon, BorderLayout.NORTH);
newPanel.add(comingSoon_Login, BorderLayout.CENTER);
//mainPane.add("Login/Register (In Progress)", comingSoon_Login);
mainPane.add("Register/Login", newPanel);
JPanel details = new JPanel(new GridLayout(0, 2));

JScrollPane accountLogScroller = new JScrollPane(accountLog);
accountLog.setLineWrap(true);
accountLog.setWrapStyleWord(true);
accountLog.setEditable(false);
JLabel delUser = new JLabel("Username to be deleted :");
JLabel delPass = new JLabel("Password :");
addHListener(delPFUser);
addHListener(delPFPass);
addHListener(newPass);
addHListener(oldPass);
addHListener(changePassName);
deleteAccount.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent ff) {
	boolean temps1 = false;
	boolean temps2 = false;
	if ((delPFUser.getText().length() >= 4) && (delPFUser.getText().length() <= 11)) {
	temps1 = true;
	}
	if ((delPFPass.getPassword().length >= 4) && (delPFPass.getPassword().length <= 11)) {
	temps2 = true;
	}
	if (temps1 && temps2) {
	writethis("DELETEACCOUNT" + delPFUser.getText() + "&" + new String(delPFPass.getPassword()));
	delPFUser.setText("");
	delPFPass.setText("");
	}
	else if (!temps1) {
	accountLog.append("\nYour username is 4-11 characters.");
	accountLog.setCaretPosition(accountLog.getDocument().getLength());
	delPFUser.setText("");
	}
	else if (!temps2) {
	accountLog.append("\nYour password is 4-11 characters.");
	accountLog.setCaretPosition(accountLog.getDocument().getLength());
	delPFPass.setText("");
	}
}
});
JLabel passOld = new JLabel("Old Password : ");
JLabel passNew = new JLabel("New Password : ");
submitChange.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent eaffa) {
	boolean temps1 = false;
	boolean temps2 = false;
	boolean temps3 = false;
	boolean temps4 = false;
	if ((newPass.getPassword().length >= 4) && (newPass.getPassword().length <= 11)) {
	temps1 = true;
	}
	if ((oldPass.getPassword().length >= 4) && (oldPass.getPassword().length <= 11)) {
	temps2 = true;
	}
	if  ((changePassName.getText().length() >= 4) && (changePassName.getText().length() <= 11)) {
	temps3 = true;
	}
	if ((!new String(newPass.getPassword()).contains("&")) && (!new String(oldPass.getPassword()).contains("&"))) {
	temps4 = true; //400
	}
	if (temps1 && temps2 && temps3 && temps4) {
	writethis("CHANGEPASSWORD" + changePassName.getText() + "&" + new String(oldPass.getPassword()) + "&" + new String(newPass.getPassword()));
	newPass.setText("");
	oldPass.setText("");
	changePassName.setText("");
	}
	else if (!temps1) {
	accountLog.append("\nYour old password is 4-11 characters.");
	accountLog.setCaretPosition(accountLog.getDocument().getLength());
	oldPass.setText("");
	}
	else if (!temps2) {
	accountLog.append("\nYour new password should be 4-11 characters.");
	accountLog.setCaretPosition(accountLog.getDocument().getLength());
	newPass.setText("");
	}
	else if (!temps3) {
	accountLog.append("\nYour name is 4-11 characters.");
	accountLog.setCaretPosition(accountLog.getDocument().getLength());
	changePassName.setText("");
	}
	else if (!temps4) {
	accountLog.append("\nWe apologize, but users with passwords containing \"&\" cannot change their password. Please delete your account and create a new one with your new password or email \"cc11rocks@yahoo.com\" your account, your old password, and your new desired password.");
	accountLog.setCaretPosition(accountLog.getDocument().getLength());
	newPass.setText("");
	oldPass.setText("");
	changePassName.setText("");
	}
	}
});
details.add(delUser);
details.add(delPFUser);
details.add(delPass);
details.add(delPFPass);
details.add(new JLabel(""));
details.add(deleteAccount);
details.add(new JLabel("Username (4-11 characters, case sensative)"));
details.add(changePassName);
details.add(passOld);
details.add(oldPass);
details.add(passNew);
details.add(newPass);
details.add(accountLogScroller);
details.add(submitChange);
mainPane.add("Account Details", details);
JPanel modOptions = new JPanel(new GridLayout(0,1));
modLog.setEditable(false);
modLog.setLineWrap(true);
modLog.setWrapStyleWord(true);
JButton getOnlineUsers = new JButton("Get online users");
getOnlineUsers.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent afae) {
writethis("modLoginRequestgetIPS");
}
});
JButton getBlockedUsers = new JButton("Get blocked users");
getBlockedUsers.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent afae) {
writethis("modLoginRequestgetBlockedUsers");
}
});
modOptions.add(getOnlineUsers);
modOptions.add(getBlockedUsers);
JScrollPane modLogScroller = new JScrollPane(modLog);
modOptions.add(modLogScroller);
mainPane.add("Mod Options(In Progress)", modOptions);
JPanel fqPanel = new JPanel();
fqPanel.setLayout(new BorderLayout());
StyledDocument fqDOC = new DefaultStyledDocument();
SimpleAttributeSet fqPaneAttrs = new SimpleAttributeSet();
StyleConstants.setAlignment(fqPaneAttrs, StyleConstants.ALIGN_CENTER);

SimpleAttributeSet answer = new SimpleAttributeSet();
answer.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(51, 0, 102));
JTextPane fqPane = new JTextPane(fqDOC);
fqPaneAttrs.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.blue);
JScrollPane fqScroll = new JScrollPane(fqPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
/****ORIG TO CHANGE
//fqPane.setParagraphAttributes(fqPaneAttrs, false);
fqPane.setCharacterAttributes(fqPaneAttrs, true); //FALSE change 20:19 9/9/11
***ORIG TO CHANGE*/
fqPane.setParagraphAttributes(fqPaneAttrs, true);
tryM(fqDOC,0,"F&Q", fqPaneAttrs);
fqPaneAttrs.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.green);
tryM(fqDOC,fqDOC.getLength(), "\n\nHow do I send a Personal Message?\n", fqPaneAttrs);
/****ORIG TO CHANGE
//fqPane.setParagraphAttributes(answer, false);
StyleConstants.setAlignment(answer, StyleConstants.ALIGN_LEFT);
***ORIG TO CHANGE*/
StyleConstants.setAlignment(answer, StyleConstants.ALIGN_LEFT);
fqPane.setParagraphAttributes(answer,true);

fqPane.setCharacterAttributes(answer, true); //FALSE change 20:10 9/9/11
tryM(fqDOC,fqDOC.getLength(), string_fq, answer);
fqPane.setCharacterAttributes(fqPaneAttrs, true); //FALSE change 20:10 9/9/11
tryM(fqDOC, fqDOC.getLength(), "Test", fqPaneAttrs);
//fqDOC.setParagraphAttributes(0, 69, fqPaneAttrs, true); //PROBABLY NOT NEEDED
//fqDOC.setParagraphAttributes(69, 270, answer, true);
//fqDOC.setParagraphAttributes(338, 10, fqPaneAttrs, true);
fqPane.setEditable(false);
fqPanel.add(fqScroll);
mainPane.add("F&Q (Not Complete)", fqPanel);
add("North", new JLabel("Comments, questions, or suggestions? Email me at \"cc11rocks@yahoo.com\"."));
add("Center", mainPane);
name.addFocusListener(new FocusListener() {
public void focusGained(FocusEvent e) {
name.selectAll();
myTempName = name.getText();
}
public void focusLost(FocusEvent af) {
if (name.getText().equals(myTempName)) {}
else {
if (name.getText().length() > 11) {
name.setText(name.getText().substring(0, 11));
}
writethis("setName" + name.getText());
}
}
});
outgoing.addKeyListener(new KeyAdapter() {
public void keyPressed(KeyEvent ea) {
if (timer.isRunning()) {
timer.stop();
timer.start();
}
else {
writethis("CURRENTLYTYPING");
timer.start();
}
switch (ea.getKeyCode()) {
case KeyEvent.VK_ENTER : 
String[] test = outgoing.getText().split(" ");
boolean tooLong = false;
	for (String x : test) {
		if (x.length() >= 100) {
			tooLong = true;
		}
	}
	if (!tooLong) {
		writethis(outgoing.getText());
		outgoing.setText("");
	}
	else {
		attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.red);
		tryM(doc, doc.getLength(), "Line too long, please break with spaces", attributes);
		attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
	}
}
}
});
onlineUsers.setPreferredSize(new Dimension(100, 480));
pnt = scroll.getLocation();
connectTo();
//if (!conected) {
//connectTo("***.***.***.***");
//}
Random rand = new Random();
int name_Int_Rand = rand.nextInt();
String newString = Integer.toString(name_Int_Rand);
if (newString.length() > 5) {
newString = newString.substring(0,5);
}
name.setText("Random" + newString);
this.addFocusListener(new FocusListener() {
public void focusGained(FocusEvent aff) {
outgoing.requestFocusInWindow();
}
public void focusLost(FocusEvent ffa) {
}
});
try {
writethis("setNameRandom" + newString);
}
catch (Exception eaff) {
outgoing.setEditable(false);
name.setEditable(false);
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.green);
tryM(doc, doc.getLength(), "Connection unsuccessful : The server may not be up. Please click outside of this Applet and push F5 to try again. The page should reload.", attributes);
goOffline();
}
}
public void connectTo() {
try {

SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
sock = (SSLSocket) sslsocketfactory.createSocket("***.***.***.***", *****);
final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
sock.setEnabledCipherSuites(enabledCipherSuites);
InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
Thread readerThread = new Thread(new IncomingReader());
readerThread.start();
reader = new BufferedReader(streamReader);
writer = new PrintWriter(sock.getOutputStream());
writethis("Connected");
}
catch (Exception ex) {
ex.printStackTrace();
}
}
private class IncomingReader implements Runnable {
public void run() {
String message;
try {
while ((message = reader.readLine()) != null) {
if (message.contains(" says : ")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.blue);
tryM(doc, doc.getLength(), getTime() + " " + message, attributes);
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
no.setCaretPosition(no.getDocument().getLength());
ping();
}
else if (message.startsWith("You said : \"")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(165,42,42));
tryM(doc, doc.getLength(), getTime() + " " + message, attributes);
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
no.setCaretPosition(no.getDocument().getLength());
ping();
}
else if (message.startsWith("onlineUserStart")) {
message = message.substring(15, message.length());
StringTokenizer st1 = new StringTokenizer(message, "&");
//onlineUsers.setText("Online Users :");
list.clear();
while (st1.hasMoreTokens()) {
list.addElement(st1.nextToken());
//onlineUsers.setText(onlineUsers.getText() + "\n" + st1.nextToken());
}
}
else if (message.equals("INNAPROPRIATEMESSAGERROR")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.red);
tryM(doc, doc.getLength(), "Your message was rejected because you entered in an inappropriate word or phrase. Please resend a cleaner message.", attributes);
no.setCaretPosition(no.getDocument().getLength());
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
ping();
}
else if (message.startsWith("USERSONLINECURRENT")) {
String appendToModLog = message.substring(18);
modLog.append("\nOnline Users : " + appendToModLog);
modLog.setCaretPosition(modLog.getDocument().getLength());
ping();
}
else if (message.equals("INNAPROPRIATENAMEERROR")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.red);
tryM(doc, doc.getLength(), "Your name was rejected because you entered in an inappropriate name. Please change your name. Your name has been changed back to the original.", attributes);
no.setCaretPosition(no.getDocument().getLength());
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
ping();
}
else if (message.equals("HACKINGDETECTEDERROR")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.red);
tryM(doc, doc.getLength(), "Please do not prepend your name with the following : \"Connected\", \"setName\", \"Null\", or \"Mod\". Please change your name. Your name has been changed back to the original.", attributes);
no.setCaretPosition(no.getDocument().getLength());
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
ping();
}
else if (message.equals("ILLEGALCHARACTERERROR")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.red);
tryM(doc, doc.getLength(), "You have used an illegal character (&, @, (, ), {, or }). Please do not use these characters in your name. Please change your name. Your name has been changed to the original.", attributes);
no.setCaretPosition(no.getDocument().getLength());
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
ping();
}
else if (message.equals("ALT255ERROR")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.red);
tryM(doc, doc.getLength(), getTime() + " Mod says : No alt+255, n00b", attributes);
no.setCaretPosition(no.getDocument().getLength());
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
ping();
}
else if (message.startsWith("GETBLOCKEDUSERS")) {
String temps = message.substring(15);
modLog.append("\n" + temps);
modLog.setCaretPosition(modLog.getDocument().getLength());
ping();
}
else if (message.startsWith("The username \"")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.red);
tryM(doc, doc.getLength(), message, attributes);
no.setCaretPosition(no.getDocument().getLength());
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
ping();
}
else if (message.equals("NAMEINUSEERROR")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.red);
tryM(doc, doc.getLength(), "Name is in use already (Online or Registered). Please choose a different name.", attributes);
no.setCaretPosition(no.getDocument().getLength());
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
ping();
}
else if (message.startsWith("LOGINSUCCESSFUL")) {
log.append("\nSuccessfully logged in as : " + message.substring(15));
log.setCaretPosition(log.getDocument().getLength());
name.setText(message.substring(15));
name.setEditable(false);
}
else if (message.equals("PASSWORDANDERROR")) {
log.append("\nYour password can not contain \"&\".");
log.setCaretPosition(log.getDocument().getLength());
rpass.setText("");
}
else if (message.equals("USERLOGGEDINFAIL")) {
log.append("\nUsername is currently being used.");
log.setCaretPosition(log.getDocument().getLength());
lname.setText("");
}
else if (message.equals("MODLOGINFAIL")) {
modLog.append("\nYou are not authorized for that action");
modLog.setCaretPosition(modLog.getDocument().getLength());
ping();
}
else if (message.equals("USERFAIL")) {
log.append("\nUsername/Password Combo Invalid.");
log.setCaretPosition(log.getDocument().getLength());
lname.setText("");
lpass.setText("");
}
else if (message.equals("SUCCESSFULACCOUNTCHANGE")) {
accountLog.append("\nYour account change was successful!");
accountLog.setCaretPosition(accountLog.getDocument().getLength());
}
else if (message.equals("FAILEDACCOUNTCHANGE")) {
accountLog.append("\nYour account change was not successful. Please try again!");
accountLog.setCaretPosition(accountLog.getDocument().getLength());
}
else if (message.equals("SUCCESSFULACCOUNTDELETE")) {
accountLog.append("\nThe account was successfully deleted!");
accountLog.setCaretPosition(accountLog.getDocument().getLength());
}
else if (message.equals("FAILEDACCOUNTDELETE")) {
accountLog.append("\nThe account was not deleted. Please try again!");
accountLog.setCaretPosition(accountLog.getDocument().getLength());
}
else if (message.equals("REGISTRATIONSUCCESSFUL")) {
log.append("\nRegistration attempt was successful...Status : Pending. Please check back in 5 minutes to see if you were accepted. Please allow up to 24 hours for acceptance.");
rpass.setText("");
rmail.setText("");
rname.setText("");
log.setCaretPosition(log.getDocument().getLength());
}
else if (message.equals("INNAPROPRIATENAMEFAIL")) {
log.append("\nYour name was rejected because you entered in an inappropriate\nname.");
rname.setText("");
log.setCaretPosition(log.getDocument().getLength());
}
else if (message.equals("HACKINGDETECTEDFAIL")) {
rname.setText("");
log.append("\nPlease do not prepend your name with the following : \"Connected\",\"setName\", \"Null\", or \"Mod\".");
log.setCaretPosition(log.getDocument().getLength());
}
else if (message.equals("ILLEGALCHARACTERFAIL")) {
rname.setText("");
log.append("\nYou have used an illegal character (&, @, (, ), {, or }) in your name.");
log.setCaretPosition(log.getDocument().getLength());
}
else if (message.equals("ALT255FAIL")) {
rname.setText("");
log.append("\n" + getTime() + " Mod says : No alt+255, n00b");
log.setCaretPosition(log.getDocument().getLength());
}
else if (message.equals("NAMEINUSEFAIL")) {
rname.setText("");
log.append("\nName is already in use (Online or Registered).");
log.setCaretPosition(log.getDocument().getLength());
}
else if (message.equals("MISSINGINFO")) {
log.append("\nYou are missing a password or an email address.");
log.setCaretPosition(log.getDocument().getLength());
}
else if (message.equals("NULLNAMEFAIL")) {
log.append("\nYou are missing a username.");
log.setCaretPosition(log.getDocument().getLength());
}
else if (message.endsWith("BLACK")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
message = replaceColor(message, "BLACK");
sendAndReset(message);
}
else if (message.endsWith("DARKGREEN")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(0, 100, 0));
message = replaceColor(message, "DARKGREEN");
sendAndReset(message);
}
else if (message.endsWith("MEDSEAGREEN")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(60,179,113));
message = replaceColor(message, "MEDSEAGREEN");
sendAndReset(message);
}
else if (message.endsWith("PURPLE")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(160,32,240));
message = replaceColor(message, "PURPLE");
sendAndReset(message);
}
else if (message.endsWith("LIGHTPINK")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(255,20,147));
message = replaceColor(message, "LIGHTPINK");
sendAndReset(message);
}
else if (message.endsWith("DARKPINK")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, new Color(139,10,80));
message = replaceColor(message, "DARKPINK");
sendAndReset(message);
}
else if (message.endsWith("YELLOW")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.YELLOW);
message = replaceColor(message, "YELLOW");
sendAndReset(message);
}
else if (message.endsWith("CYAN")) {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.CYAN);
message = replaceColor(message, "CYAN");
sendAndReset(message);
}
else if (message.startsWith("onlineUserType")) {
	try {
		message = message.substring(14);
		if (message.equals("")) {
			//typingUsers.setVisible(false);
			typingUsers.setText("");
			no.setCaretPosition(no.getDocument().getLength());
		}
		else {
			typingUsers.setText(message);
			//typingUsers.setVisible(true);
			no.setCaretPosition(no.getDocument().getLength());
		}
	}
	catch (Exception eat) {
		tryM(doc, doc.getLength(), eat.getMessage().toString(), attributes);
		no.setCaretPosition(no.getDocument().getLength());
	}
}
else {
sendAndReset(message);
}
}
//try {
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.green);
tryM(doc, doc.getLength(), getTime() + "\nYou are not connected to the server. Please refresh the page (click outside of Applet + push F5).", attributes);
//}
goOffline();
//catch (Exception fff) {}
}
catch (Exception exaf) {
exaf.printStackTrace();
//try {

//*****************8
attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.green);
tryM(doc, doc.getLength(), getTime() + " " + "\nYou are not connected to the server. Please refresh the page (click outside of Applet + push F5).", attributes);
goOffline();
//*8888************8
 
//}
//catch (Exception fjfjf) {}
}
}
}
public void ping(){
	try {
	//FOR JAR
	//URL url = this.getClass().getClassLoader().getResource("Ping.au");
	URL url = this.getClass().getClassLoader().getResource("chime.wav"); //Ping.au = old sound
	AudioInputStream sound = AudioSystem.getAudioInputStream(url); //new File("Audio/ping.au")
	//FOR FILE
	//AudioInputStream sound = AudioSystem.getAudioInputStream(new File("ping.au"));
    // load the sound into memory (a Clip)
    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
    clip1 = (Clip) AudioSystem.getLine(info);
    clip1.open(sound);
	
	// Set Volume
FloatControl gainControl = (FloatControl)clip1.getControl(FloatControl.Type.MASTER_GAIN);
double gain = soundD;    // number between 0 and 1 (loudest)
float dB = (float)(Math.log(gain)/Math.log(10.0)*20.0);
gainControl.setValue(dB);

    // due to bug in Java Sound, explicitly exit the VM when
    // the sound has stopped.
	/* DELETED TEMP
    clip.addLineListener(new LineListener() {
      public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
          event.getLine().close();
        }
      }
    });
	*/ //DELETED TEMP
    // play the sound clip
	clip1.start();
  }
  catch (Exception e) {
  e.printStackTrace();
  }
  }
  private void tryM (StyledDocument textPaneU, int integerF, String stringA, SimpleAttributeSet attributesJ) {
  try {
  textPaneU.insertString(integerF, "\n" + stringA, attributesJ);
  }
  catch(Exception eafff) {
  eafff.printStackTrace();
  }
  }
  private String getTime() {
  String realDate = "";
  long timeInMillis = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		int date = cal.get(Calendar.MINUTE);
		int date1 = cal.get(Calendar.HOUR);
		if (date1 <= 9 || date <= 9) {
			if (date1 <9 && date <= 9) {
			String tempEr = "0" + Integer.toString(date1);
			String tempEr_1 = "0" + Integer.toString(date);
			realDate = tempEr + ":" + tempEr_1;
			}
			else if (date1 <= 9) {
			String tempEr = "0" + Integer.toString(date1);
			String tempEr_1 = Integer.toString(date);
			realDate = tempEr + ":" + tempEr_1;
			}
			else if (date <=9) {
			String tempEr = Integer.toString(date1);
			String tempEr_1 = "0" + Integer.toString(date);
			realDate = tempEr + ":" + tempEr_1;
			}
		}
		else {
		realDate = Integer.toString(date1) + ":" + Integer.toString(date);
  }
return realDate;
}
	private void addHListener(final JTextComponent a) {
	a.addFocusListener(new FocusListener() {
		public void focusLost(FocusEvent fa) {
		}
		public void focusGained(FocusEvent af) {
		a.selectAll();
		}
	});
	}
	private void goOffline() {
	submitChange.setEnabled(false);
	newPass.setEditable(false);
	oldPass.setEditable(false);
	changePassName.setEditable(false);
	delPFUser.setEditable(false);
	delPFPass.setEditable(false);
	deleteAccount.setEnabled(false);
	accountLog.append("\nOffline.");
	lname.setEditable(false);
	lpass.setEditable(false);
	loginButton.setEnabled(false);
	submit.setEnabled(false);
	list.clear();
	name.setEditable(false);
	outgoing.setEditable(false);
	rname.setEditable(false);
	rpass.setEditable(false);
	rmail.setEditable(false);
	log.append("\nYou are offline.");
	log.setCaretPosition(log.getDocument().getLength());
	list.addElement("Offline");
	attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
	no.setCaretPosition(no.getDocument().getLength());
	ping();
	}
	private String replaceColor(String receivedString, String colorToReplace) {
	StringBuilder colorBuilder = new StringBuilder(receivedString);
	colorBuilder.replace(receivedString.lastIndexOf(colorToReplace), receivedString.lastIndexOf("") + 1, "");
	return colorBuilder.toString();
	}
	private void setButtonAction(JButton colorButton, final String stupidHack) {
	colorButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent buts) {
		writethis("setColor" + stupidHack);
		}
	});
	}
	private void sendAndReset(String message) {
	tryM(doc, doc.getLength(), getTime() + " " + message, attributes);
	attributes.addAttribute(StyleConstants.CharacterConstants.Foreground, Color.black);
	no.setCaretPosition(no.getDocument().getLength());
	ping();
	}
	private void writethis(String toWriter) {
	writer.println(toWriter);
	writer.flush();
	}
}
