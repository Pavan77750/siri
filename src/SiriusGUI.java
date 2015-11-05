import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SiriusGUI {
    
    private JLabel updatingLabel;
    private JButton startButton;    
    private JButton stopButton;
    private JButton okButton;
    private JFrame frame;
    JTextArea editArea;
    public boolean start;

    private MyThread myThread;
    
    private ActionListener buttonActions = new ActionListener()
     {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if (ae.getSource() == startButton)
            {
                if(start==false)
                {
                    myThread = new MyThread(SiriusGUI.this);
                    start=true;
                    okButton.setEnabled(true);
                    stopButton.setEnabled(true);
                }
            }           
            else if (ae.getSource() == stopButton)
            {
                if (myThread != null) 
                {
                    myThread.myStop();
                    try {
                        myThread.t.join();
                    }
                    catch(InterruptedException ie) {ie.printStackTrace();}
                }
                System.exit(0);
            }
            else if (ae.getSource() == okButton)
            {
                myThread.execute(editArea.getText());
                editArea.append("\nSay Another commands.....");
            }
        }
    };
    

    public SiriusGUI()
    {
        start=false;
    }
    private void performTask()
    {
        frame = new JFrame("Sirius Personal Assistant.");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel contentPane = new JPanel(new BorderLayout(5, 5));
        updatingLabel = new JLabel("Sirius Personal Assistant", JLabel.CENTER);
        
        startButton = new JButton("Start");
        startButton.addActionListener(buttonActions);       
        stopButton = new JButton("Stop");
        stopButton.addActionListener(buttonActions);
        okButton = new JButton("Ok");
        okButton.addActionListener(buttonActions);

        okButton.setEnabled(false);
        stopButton.setEnabled(false);


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);       
        buttonPanel.add(stopButton);
        buttonPanel.add(okButton);
        
        editArea = new JTextArea(5,40);
        Font font = new Font(
                        Font.MONOSPACED, 
                        Font.PLAIN, 
                        editArea.getFont().getSize());
        editArea.setFont(font);
        JPanel textPanel = new JPanel();
        editArea.setPreferredSize(new Dimension(200,100));
        textPanel.add(editArea);
        editArea.setText("This is a speech recognition test.\n"  
            + "press Start to get started\n"+"Speak 'stop' to quit.\n"+"Say some commands.......");

        JPanel titlePanel=new JPanel();
        titlePanel.add(updatingLabel);

        contentPane.add(titlePanel, BorderLayout.PAGE_START);
        contentPane.add(textPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);
        
        
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
    
    public JLabel getLabel() 
    {
        return updatingLabel;
    }
    public JFrame getFrame() 
    {
        return frame;
    }
    
    public static void main(String[] args)
    {
        new SiriusGUI().performTask();       
    }
    public void setText(String s)
    {
        editArea.setText(s);
    }
}

class MyThread implements Runnable {
    
    private volatile boolean running;
    private volatile boolean suspendThread;
    private String s;
    private SiriusGUI sst;
    public Thread t;
    
    public MyThread(SiriusGUI sst) 
    {
        voce.SpeechInterface.init("../lib", false, true,"./grammar", "action");
        this.sst = sst;
        running = true;
        suspendThread = false;
        t = new Thread(this, "MyThread");
        t.start();
    }
    
    @Override
    public void run() 
    {
        System.out.println("This is a speech recognition test. "  
            + "Speak 'stop' to quit.");

        while (running) 
        {
            while (voce.SpeechInterface.getRecognizerQueueSize() > 0)
            {
                s = voce.SpeechInterface.popRecognizedString();
                System.out.println("text recognised: " + s);
                sst.setText(s);
                boolean done=execute(s);
                String temp;
                if(done == true)
                {
                     try
                    {
                        temp="\"You said " + s +"\"";
                        Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","espeak "+temp});
                    }
                    catch(Exception e){};        
                }
                else
                {
                    try
                    {
                        temp="\"You said " + s +"\"";
                        Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","espeak "+temp});
                        try{
                            Thread.sleep(2000);
                        }catch(Exception e){}
                        temp="\"No such command exist\"";
                        p = Runtime.getRuntime().exec(new String[]{"bash","-c","espeak "+temp});
                    }
                    catch(Exception e){}
                }

        
                Runnable runnable = new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        (sst.getLabel()).setText("You said: "+s);
                    }
                };

                EventQueue.invokeLater(runnable);

                try
                {
                    Thread.sleep(500);
                }
                catch(InterruptedException ie) 
                {
                    System.out.format("Thread Interrupted :(%n Message : %s%n", ie.getMessage());
                    ie.printStackTrace();
                }
                
                synchronized(this) 
                {
                    if (suspendThread)
                     {
                        try
                        {
                            wait();
                        } catch(InterruptedException ie) 
                        {
                            System.out.format("Waiting Thread Interrupted"
                                                + ":(%n Message : %s%n"
                                                , ie.getMessage());
                        }
                    }
                }
            }
        }
        voce.SpeechInterface.destroy();
        System.exit(0);
    }
    public boolean execute(String s)
    {
        boolean done=false;
        if (-1 < s.indexOf("stop"))
        {
            running=false;
            done=true;
        }
        else if(-1< s.indexOf("open") && -1 < s.indexOf("calculator"))
        {
            try
            {
               Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","gnome-calculator"});
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            done=true;
        }
        else if (-1 < s.indexOf("open") && -1 < s.indexOf("browser"))
        {
            try
            {
               Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","firefox"});
            }
            catch(Exception e)
            {

            }
             done=true;
        }
        else if (-1 < s.indexOf("list") && -1 < s.indexOf("file"))
        {
            System.out.print("HDGAGD ASJDAS ");
            try
            {
               Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "ls -l > temp.txt"});
                p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "gedit temp.txt"});
            }
            catch(Exception e)
            {

            }
            done=true;
        }
        else if(-1 < s.indexOf("search"))
        {
            int i=s.indexOf("search")+6;
            String temp="";
            for(;i<s.length();i++)
                if(s.charAt(i)!=' ')temp+=s.charAt(i);
            System.out.println(temp);
            String cmd="firefox"+" "+"\"https://www.google.co.in/webhp#q="+temp+"&ddle=0\"";
            try{
            Process p = Runtime.getRuntime().exec(new String[]{"bash","-c",cmd});
            }catch(Exception e){};

             done=true;

        }
        else if(-1 < s.indexOf("open") && -1<s.indexOf("terminal") )
        {
            try{
            Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","terminator"});
            }catch(Exception e){};
            done=true;
        }
        else if (-1 < s.indexOf("show") && -1 < s.indexOf("show"))
        {
             sst.getFrame().setVisible(true);
        }

        return done;
    }


    public void mySuspend() {
        suspendThread = true;
    }
    
    public synchronized void myResume() {
        suspendThread = false;
        notify();
    }
    
    public void myStop() 
    {
        if (suspendThread) myResume();
        running = false;
    }
}