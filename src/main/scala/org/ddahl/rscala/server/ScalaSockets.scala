package org.ddahl.rscala.server

import java.net._
import java.io._

import Protocol._

private[rscala] class ScalaSockets(portsFilename: String, port: Int, debugger: Debugger) {

  val serverIn  = new ServerSocket(port,1,InetAddress.getByName(null))
  val serverOut = new ServerSocket(if ( port == 0 ) 0 else port+1,1,InetAddress.getByName(null))

  if ( debugger.value ) debugger.msg("Trying to open ports filename: "+portsFilename)
  locally {
    val portNumberFile = new File(portsFilename)
    val p = new PrintWriter(portNumberFile)
    p.println(serverIn.getLocalPort+" "+serverOut.getLocalPort)
    p.close()
  }
  if ( debugger.value ) debugger.msg("Servers are running on port "+serverIn.getLocalPort+" "+serverOut.getLocalPort)

  val socketIn = serverIn.accept
  socketIn.setTcpNoDelay(true)
  val in = new DataInputStream(new BufferedInputStream(socketIn.getInputStream))
  val socketOut = serverOut.accept
  socketOut.setTcpNoDelay(true)
  val out = new DataOutputStream(new BufferedOutputStream(socketOut.getOutputStream))

}

