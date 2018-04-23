package com.tochi.RobotMuchasBolasRETRO;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class RobotActivity extends Activity {

    // properties to handle the contol buttons, once the device is connected
    // START:

    public int conectado=0;

    public int grabando=0;


    public int timestapInicioGrabacion=0;
    public int secuenciaSig=0;

    View viewTemporal;


    ArrayList<PasosRutina> nuevaLista= new ArrayList<PasosRutina>();

    //END

    //Properties- to handle to BT connecting and detection
    //START
    private BluetoothAdapter myBluetooth = null;
    private Set pairedDevices;

    Button btnPaired;
    Button btnRobotsEnlazados;

    ListView devicelist;

    ListView devicelistRobotsEnlazados;

    Button btnOn, btnOff, btnDis;

    String address = null;

    BluetoothSocket btSocket = null;

    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //END


    List BTbtSocket =new ArrayList();
    List BTisBtConnected =new ArrayList();


    List BTrobotsDisponiblesAddress =new ArrayList();
    List BTrobotsDisponiblesName =new ArrayList();

    List BTposicionRobotDisponibles =new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);



//        viewTemporal=inflater.inflate(R.layout.fragment_tercer, container, false);
        Button btnSaveTaskLocal=(Button)  findViewById(R.id.conectar);

//        EditText nombre= (EditText) findViewById(R.id.nombreRutina);
//        nombre.setVisibility(View.INVISIBLE);

        System.out.println("onCreateView paso conectar 1");

        btnSaveTaskLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onCreateView paso conectar 1.1");
                Button tiny = (Button)v.findViewById(R.id.conectar);

                Button btnPairedEsconder;
                ListView devicelistEsconder;
                ListView devicelistEsconderRobotsEnlazados;
                btnPairedEsconder = (Button)findViewById(R.id.btnBuscarDispBlue02);
                devicelistEsconder = (ListView)findViewById(R.id.lst_dispositivos02);

                devicelistEsconderRobotsEnlazados = (ListView)findViewById(R.id.lst_dispositivos03);

                Button btnEnlazarRobotsEsconder;
                btnEnlazarRobotsEsconder = (Button)findViewById(R.id.btnListarDispBlue03);

//                Button btnGrabar=(Button)  findViewById(R.id.Grabar);
                Button btnUp02=(Button)  findViewById(R.id.botonUp);
                Button btnDown02=(Button)  findViewById(R.id.botonDown);
                Button btnRight02=(Button)  findViewById(R.id.botonRight);
                Button btnLeft02=(Button)  findViewById(R.id.botonLeft);
//                Button tiny04 = (Button)findViewById(R.id.InicioGrabar);
//                Button tiny05 = (Button)findViewById(R.id.FinGrabar);

                System.out.println("onCreateView paso conectar 1.2");

                if(conectado==0) {//conecta con robot bola
                    System.out.println("onCreateView paso conectar 1.3");
                    tiny.setBackgroundResource(R.mipmap.ic_conectadoconbolaok);
                    btnPairedEsconder.setVisibility(View.INVISIBLE);
                    devicelistEsconder.setVisibility(View.INVISIBLE);

                    devicelistEsconderRobotsEnlazados.setVisibility(View.VISIBLE);//this list must be shown with the conectar button is clicked and 0

                    btnEnlazarRobotsEsconder.setVisibility(View.INVISIBLE);
//                    btnGrabar.setVisibility(View.VISIBLE);
                    btnUp02.setVisibility(View.VISIBLE);
                    btnDown02.setVisibility(View.VISIBLE);
                    btnRight02.setVisibility(View.VISIBLE);
                    btnLeft02.setVisibility(View.VISIBLE);
                    conectado=1;
                }else{ //desconecta con robot bola
                    System.out.println("onCreateView paso conectar 1.4");
                    tiny.setBackgroundResource(R.mipmap.ic_conectarconbola);
                    btnPairedEsconder.setVisibility(View.VISIBLE);
                    btnEnlazarRobotsEsconder.setVisibility(View.VISIBLE);
                    devicelistEsconder.setVisibility(View.VISIBLE);

                    devicelistEsconderRobotsEnlazados.setVisibility(View.INVISIBLE);//this list must be hidden , so the
                                                                                     //other list BT is  shown when the conectar button is clicked
//                    btnGrabar.setVisibility(View.INVISIBLE);
                    btnUp02.setVisibility(View.INVISIBLE);
                    btnDown02.setVisibility(View.INVISIBLE);
                    btnRight02.setVisibility(View.INVISIBLE);
                    btnLeft02.setVisibility(View.INVISIBLE);
                    conectado=0;
                }
                System.out.println("conectar button setOnClickListener paso conectar 1. 5");


                ///display the list of connected robots
                if (BTrobotsDisponiblesName.size()>0) //are ther any robots connected
                {
                    System.out.println("conectar button setOnClickListener paso 24");

                    int cuantosRobots = BTrobotsDisponiblesName.size();

                    ArrayList list = new ArrayList();
                    list.add("Lista Robots Enlazados");
                    for(int i=0; i<cuantosRobots; i++){
                        System.out.println("conectar button setOnClickListener:paso 1.2.1:"+i);
                        String robotEnlazado = (String) BTrobotsDisponiblesName.get(i);
                        System.out.println("conectar button setOnClickListener paso 25:"+robotEnlazado);
                        if(list.indexOf(robotEnlazado)<0) {
                            list.add(robotEnlazado); //Get the device's name and the address
                        }
                        System.out.println("conectar button setOnClickListener paso 26");

                    }

                    System.out.println("conectar button setOnClickListener paso 29"+list.size());
                    final ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_list_item_1, list);
                    System.out.println("conectar button setOnClickListener paso 29.1");
                    devicelistRobotsEnlazados.setAdapter(adapter);
                    System.out.println("conectar button setOnClickListener paso 29.2");
                    adapter.notifyDataSetChanged();
                    System.out.println("conectar button setOnClickListener paso 29.3");


                }

            }
        });







/////////////////////////////////////botones de control ///////////////////////////////////////////////////77


        Button btnControlUp=(Button)  findViewById(R.id.botonUp);
        btnControlUp.setVisibility(View.INVISIBLE);// que no se vea de inicio
        btnControlUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Up:");
                if(conectado==1) {//hay conexion con el  con robot bola
                    //manda comando a Robot Bola
                    //START: send comando to arduino
                    System.out.println("turn UP Command 1");

                    //for each BTbtSocket.get(i)
                    int cuantosRobots=BTbtSocket.size();
                    System.out.println("Up:paso 1.1");
                    if( cuantosRobots>0){
                        System.out.println("Up:paso 1.2");
                        for(int i=0; i<cuantosRobots; i++){
                            System.out.println("Up:paso 1.2.1:"+i);
                            BluetoothSocket tempBtSocket = (BluetoothSocket) BTbtSocket.get(i);
                            System.out.println("Up:paso 1.2.2:");
                            if (tempBtSocket!=null)
                            {
                                System.out.println("turn UP Command 2");
                                try
                                {
                                    tempBtSocket.getOutputStream().write("F".toString().getBytes());
                                    System.out.println("turn UP Command 3");
                                }
                                catch (IOException e)
                                {
                                    msg("Error");
                                }
                                System.out.println("turn UP Command 4");
                            }
                            System.out.println("turn UP Command 5");
                            //END: send comando to arduino


                        }//end for
                        System.out.println("Up:paso 1.3 despues del for");

                    }//end cuantosrobots
                    System.out.println("Up:paso 1.4 despues de if cuantosRobots");
                }else{ //desconectado del robot bola
                    //no hacer nada
                    System.out.println("Up:paso 1.5desconectado del robot no hacer nada");
                }
/*                    if (btSocket!=null)
                    {
                        System.out.println("turn UP Command 2");
                        try
                        {
                            btSocket.getOutputStream().write("F".toString().getBytes());
                            System.out.println("turn UP Command 3");
                        }
                        catch (IOException e)
                        {
                            msg("Error");
                        }
                        System.out.println("turn UP Command 4");
                    }
                    System.out.println("turn UP Command 5");
                    //END: send comando to arduino

                }else{ //desconectado del robot bola
                    //no hacer nada
                }
*/
            }
        });


        Button btnControlDown=(Button)  findViewById(R.id.botonDown);
        btnControlDown.setVisibility(View.INVISIBLE);// que no se vea de inicio
        btnControlDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Down:");
                if(conectado==1) {//hay conexion con el  con robot bola

                    //manda comando a Robot Bola
                    //START: send comando DOWN to arduino
                    System.out.println("turn DOWN Command 1");
                    //for each BTbtSocket.get(i)
                    int cuantosRobots=BTbtSocket.size();
                    System.out.println("Down:paso 1.1");
                    if( cuantosRobots>0) {
                        System.out.println("Down:paso 1.2");
                        for (int i = 0; i < cuantosRobots; i++) {
                            System.out.println("Down:paso 1.2.1:" + i);
                            BluetoothSocket tempBtSocket = (BluetoothSocket) BTbtSocket.get(i);
                            System.out.println("Down:paso 1.2.2:");
                            if (tempBtSocket!=null)
                            {
                                System.out.println("turn DOWN Command 2");
                                try
                                {
                                    tempBtSocket.getOutputStream().write("B".toString().getBytes());
                                    System.out.println("turn DOWN Command 3");
                                }
                                catch (IOException e)
                                {
                                    msg("Error");
                                }
                                System.out.println("turn DOWN Command 4");
                            }
                            System.out.println("turn DOWN Command 5");
                            //END: send comando to arduino


                        }
                    }
                }else{ //desconectado del robot bola
                    //no hacer nada
                }

            }
        });


        Button btnControlRigth=(Button)  findViewById(R.id.botonRight);
        btnControlRigth.setVisibility(View.INVISIBLE);//que no se vena de inicio
        btnControlRigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Right:");
                if(conectado==1) {//hay conexion con el  con robot bola
                    //manda comando a Robot Bola
                    //START: send comando DOWN to arduino
                    System.out.println("turn RIGHT Command 1");

                    //for each BTbtSocket.get(i)
                    int cuantosRobots=BTbtSocket.size();
                    System.out.println("RIGHT:paso 1.1");
                    if( cuantosRobots>0) {
                        System.out.println("RIGHT:paso 1.2");
                        for (int i = 0; i < cuantosRobots; i++) {
                            System.out.println("RIGHT:paso 1.2.1:" + i);
                            BluetoothSocket tempBtSocket = (BluetoothSocket) BTbtSocket.get(i);
                            System.out.println("RIGHT:paso 1.2.2:");

                            if (tempBtSocket!=null)
                            {
                                System.out.println("turn RIGHT Command 2");
                                try
                                {
                                    tempBtSocket.getOutputStream().write("R".toString().getBytes());
                                    System.out.println("turn RIGHT Command 3");
                                }
                                catch (IOException e)
                                {
                                    msg("Error");
                                }
                                System.out.println("turn RIGHT Command 4");
                            }
                            System.out.println("turn RIGHT Command 5");

                        }
                    }
                    //END: send comando to arduino
                }else{ //desconectado del robot bola
                    //no hacer nada
                }

            }
        });


        Button btnControlLeft=(Button)  findViewById(R.id.botonLeft);
        btnControlLeft.setVisibility(View.INVISIBLE);// que no se vea de inicio
        btnControlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Left:");
                if(conectado==1) {//hay conexion con el  con robot bola


                    //manda comando a Robot Bola
                    //START: send comando LEFT to arduino
                    System.out.println("turn LEFT Command 1");

                    //for each BTbtSocket.get(i)
                    int cuantosRobots=BTbtSocket.size();
                    System.out.println("RIGHT:paso 1.1");
                    if( cuantosRobots>0) {
                        System.out.println("LEFT:paso 1.2");
                        for (int i = 0; i < cuantosRobots; i++) {
                            System.out.println("LEFT:paso 1.2.1:" + i);
                            BluetoothSocket tempBtSocket = (BluetoothSocket) BTbtSocket.get(i);
                            System.out.println("LEFT:paso 1.2.2:");
                            if (tempBtSocket!=null)
                            {
                                System.out.println("turn LEFT Command 2");
                                try
                                {
                                    tempBtSocket.getOutputStream().write("L".toString().getBytes());
                                    System.out.println("turn LEFT Command 3");
                                }
                                catch (IOException e)
                                {
                                    msg("Error");
                                }
                                System.out.println("turn LEFT Command 4");
                            }
                            System.out.println("turn LEFT Command 5");

                        }

                    }
                    //END: send comando to arduino
                }else{ //desconectado del robot bola
                    //no hacer nada
                }

            }
        });



/////////////////////////////////////botones/funcionalidad de connecting BT ///////////////////////////////////////////////////77
        btnPaired = (Button)findViewById(R.id.btnBuscarDispBlue02);

        devicelist = (ListView)findViewById(R.id.lst_dispositivos02);

        System.out.println("onCreateView paso 100000031");
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        System.out.println("onCreateView paso 32");
        if(myBluetooth == null)
        {
            System.out.println("onCreateView paso 33");
            //Show a mensag. that thedevice has no bluetooth adapter
//            Toast.makeText(viewTemporal.getContext().getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            Toast.makeText(this.getBaseContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            System.out.println("onCreateView paso 34");
            //finish apk
            //   finish();
        }
        else
        {
            System.out.println("onCreateView paso 10000035");
            if (myBluetooth.isEnabled())
            {
                System.out.println("onCreateView paso 100000 36:Exioto!!!");
            }
            else
            {
                System.out.println("onCreateView paso 37: no, pedir que activen bluetooth");
                //Ask to the user turn the bluetooth on
                //Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //startActivityForResult(turnBTon,1);

            }
        }
        System.out.println("onCreateView paso 40:boton listener!!!");
        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                System.out.println("onCreateView paso 50");
                pairedDevicesList(v); //method that will be called
                System.out.println("onCreateView paso 60");
            }
        });

/////////////////////////////////////botones/funcionalidad de Listar BT  robots///////////////////////////////////////////////////77
        btnRobotsEnlazados = (Button)findViewById(R.id.btnListarDispBlue03);

        devicelistRobotsEnlazados = (ListView)findViewById(R.id.lst_dispositivos03);

        ListView laList03 = (ListView)findViewById(R.id.lst_dispositivos03);
        laList03.setVisibility(View.INVISIBLE);


        System.out.println("onCreateView paso 70");

        System.out.println("onCreateView paso 72:boton listener!!!");


        System.out.println("robotsEnlazadosDevicesList paso 40:boton listener!!!");
        btnRobotsEnlazados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (BTrobotsDisponiblesName.size()>0) //are ther any robots connected
                {
                    System.out.println("robotsEnlazadosDevicesList paso 24");

                    int cuantosRobots = BTrobotsDisponiblesName.size();

                    ArrayList list = new ArrayList();
                    list.add("Lista Robots Enlazados");
                    for(int i=0; i<cuantosRobots; i++){
                        System.out.println("robotsEnlazadosDevicesList:paso 1.2.1:"+i);
                        String robotEnlazado = (String) BTrobotsDisponiblesName.get(i);
                        System.out.println("robotsEnlazadosDevicesList paso 25:"+robotEnlazado);
                        if(list.indexOf(robotEnlazado)<0) {
                            list.add(robotEnlazado); //Get the device's name and the address
                        }
                        System.out.println("robotsEnlazadosDevicesList paso 26");

                    }

                    System.out.println("robotsEnlazadosDevicesList paso 29"+list.size());
                    final ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_list_item_1, list);
                    System.out.println("robotsEnlazadosDevicesList paso 29.1");
                    devicelistRobotsEnlazados.setAdapter(adapter);
                    System.out.println("robotsEnlazadosDevicesList paso 29.2");
                    adapter.notifyDataSetChanged();
                    System.out.println("robotsEnlazadosDevicesList paso 29.3");


                }
                System.out.println("robotsEnlazadosDevicesList paso 50");
                Button btnLista=(Button)  findViewById(R.id.btnBuscarDispBlue02);
                btnLista.setVisibility(View.INVISIBLE);// que no se vea de inicio

                ListView laList = (ListView)findViewById(R.id.lst_dispositivos02);
                laList.setVisibility(View.INVISIBLE);

                System.out.println("robotsEnlazadosDevicesList paso 60");
                ListView laList03 = (ListView)findViewById(R.id.lst_dispositivos03);
                laList03.setVisibility(View.VISIBLE);
                System.out.println("robotsEnlazadosDevicesList paso 70");
            }
        });


    }

    ////////////////////////////////////////////////////////////////////////////////////
    //--method-----------------------------------------------lista paired BT --------------------
    /** Called to get the list of bluetooth devices paired with the mobile */
    ////////////////////////////////////////////////////////////////////////////////////
    private void pairedDevicesList(View v)
    {
        //pairedDevices = myBluetooth.getBondedDevices();
        System.out.println("pairedDevicesList paso 21");
        Set<BluetoothDevice> pairedDevices =myBluetooth.getBondedDevices();
        System.out.println("pairedDevicesList paso 22");
        ArrayList list = new ArrayList();
        System.out.println("pairedDevicesList paso 23");
        if (pairedDevices.size()>0)
        {
            System.out.println("pairedDevicesList paso 24");
            for(BluetoothDevice bt : pairedDevices)
            {
                System.out.println("pairedDevicesList paso 25");
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
                System.out.println("pairedDevicesList paso 26"+bt.getAddress());
            }
            System.out.println("pairedDevicesList paso 27");
        }
        else
        {
            System.out.println("pairedDevicesList paso 28");
            Toast.makeText(v.getContext().getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
        System.out.println("pairedDevicesList paso 29");
        final ArrayAdapter adapter = new ArrayAdapter(v.getContext(),android.R.layout.simple_list_item_1, list);
        System.out.println("pairedDevicesList paso 29.1");
        devicelist.setAdapter(adapter);
        System.out.println("pairedDevicesList paso 29.2");
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

        adapter.notifyDataSetChanged();
        System.out.println("onCreateView paso 29.3");
    }




    ////////////////////////////////////////////////////////////////////////////////////
    //--method-----------------------------------------------que manda a guardar a la web --------------------
    /** Called when the user clicks the goToAddEdificio  button */
    ////////////////////////////////////////////////////////////////////////////////////
    public void goToConectar(View view) {

        //get the result data
        String resultData = null;

        System.out.println("goToAddEdificio - 1");



    }
    private void msg(String s)
    {
        System.out.println("msg 1"+s);
        // Toast.makeText(getContext().getApplicationContext(),s,Toast.LENGTH_LONG).show();
        System.out.println("msg 2");
    }


    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    // se usa para click en cada elemento Paired de Bluettoth
    ////////////////////////////////////////////////////////////////////////////////////7
    ////////////////////////////////////////////////////////////////////////////////////
    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            System.out.println("myListClickListener onItemClick paso 0");

            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            System.out.println("myListClickListener onItemClick paso 1, el valor del arg2 es:"+arg2);

            address = info.substring(info.length() - 17);

            if(BTrobotsDisponiblesAddress.size()>0 ){
                System.out.println("myListClickListener onItemClick paso 1.8:s hay algun robot enlazado antees");
                if(BTrobotsDisponiblesAddress.contains("address")){
                    //ya esta este robot en la lista de Robots Enlazados

                    System.out.println("myListClickListener onItemClick paso 2:ya esta este robot en la lista de Robots Enlazados"+address);
                }else{
                    //add the robot name and address
                    BTrobotsDisponiblesAddress.add(address);//add the address to the List of Robots

                    String nameBTdevice = info.substring(0, info.length() - 17); //for first character to where the MAC address starts
                    BTrobotsDisponiblesName.add(nameBTdevice);//add the address to the List of Robots
                    System.out.println("myListClickListener onItemClick paso 2"+address);

                    isBtConnected=false;//desconnect the current connection, so we can switch to another BlueTooth device

                    //call the widgtes to enlazar el siguiente  robot
                    ConnectBT bt  = new ConnectBT() ;
                    System.out.println("myListClickListener onItemClick paso 3");
                    bt.cualSocket=arg2;
                    bt.execute();
                    System.out.println("myListClickListener onItemClick paso 4");

                }
            }else{
                //no robots enlazados yet
                //add the first robot name and address
                BTrobotsDisponiblesAddress.add(address);//add the address to the List of Robots

                String nameBTdevice = info.substring(0, info.length() - 17); //for first character to where the MAC address starts
                BTrobotsDisponiblesName.add(nameBTdevice);//add the address to the List of Robots
                System.out.println("myListClickListener onItemClick paso 2"+address);

                isBtConnected=false;//desconnect the current connection, so we can switch to another BlueTooth device

                //call the widgtes to enlazar el primer robot
                ConnectBT bt  = new ConnectBT() ;
                System.out.println("myListClickListener onItemClick paso 3");
                bt.cualSocket=arg2;
                bt.execute();
                System.out.println("myListClickListener onItemClick paso 4");

            }



        }
    };


    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    // se usa para conectar al BLuetooth
    ////////////////////////////////////////////////////////////////////////////////////7
    ////////////////////////////////////////////////////////////////////////////////////

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected
        public int cualSocket=0;
        @Override
        protected void onPreExecute()
        {
            System.out.println("ConnectBT onPreExecute paso 1");
//            progress = ProgressDialog.show(getContext().getApplicationContext(), "Connecting...", "Please wait!!!");  //show a progress dialog
            System.out.println("ConnectBT onPreExecute paso 2");
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                System.out.println("ConnectBT doInBackground paso 1");



                int existe= BTposicionRobotDisponibles.indexOf(cualSocket+"");//obtain the index of this element

                System.out.println("ConnectBT doInBackground paso 2:previo al if "+existe);
//                if (btSocket == null || !isBtConnected)
                if ( BTposicionRobotDisponibles!=null || existe> 0) //use the value as flag, if the robot has not been added yet
                {
                    System.out.println("ConnectBT doInBackground paso 2");
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    System.out.println("ConnectBT doInBackground paso 3"+address);
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    System.out.println("ConnectBT doInBackground paso 4");
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    System.out.println("ConnectBT doInBackground paso 5");
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    System.out.println("ConnectBT doInBackground paso 6");
                    btSocket.connect();//start connection
                    System.out.println("ConnectBT doInBackground paso 7");

                    BTbtSocket.add(btSocket);// this adds the scoekt into the lists of sockets Enlcae con cada robot
                    BTposicionRobotDisponibles.add(cualSocket+"");//posicioon del robot en la lista original de dispositivos BT disponible
                    System.out.println("ConnectBT doInBackground paso 7.4");

//                }else if(!isBtConnected){//previous conection
                }else {//previous conection
                    System.out.println("ConnectBT doInBackground paso 7.55: ese robot ya estaba dado de alta enlazados");
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                System.out.println("ConnectBT doInBackground paso 8");
            }
            System.out.println("ConnectBT doInBackground paso 9");
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            System.out.println("ConnectBT onPostExecute paso 1");
            super.onPostExecute(result);
            System.out.println("ConnectBT onPostExecute paso 2");
            if (!ConnectSuccess)
            {
                System.out.println("ConnectBT onPostExecute paso 3");
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                System.out.println("ConnectBT onPostExecute paso 4");
//                finish();
            }
            else
            {
                System.out.println("ConnectBT onPostExecute paso 5");
                msg("Connected.");
                isBtConnected = true;
            }
            System.out.println("ConnectBT onPostExecute paso 6");
            //progress.dismiss();
            System.out.println("ConnectBT onPostExecute paso 7");
        }

    }
}
