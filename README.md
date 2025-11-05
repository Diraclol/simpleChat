#SEG 2105 - Assignment 2 Submission<br>
###Daniel Nguyen<br>
###ST# 300420133<br>
Submission code works with *Eclipse IDE 2025-09* for testing and function<br>
Test Cases are also included with submission<br>

## Test Cases
| Test Case | Description | Instructions | Expected Result | Cleanup | Status |
|---|---|---|---|---|---|
| **2001** | Server startup check | Start the server program. | Server prints: `Server listening for clients on port 5555` and waits for input. | Terminate the server. | ✓ |
| **2002** | Client start without login | Start the client without specifying loginID. | Client prints: `ERROR - No login ID specified. Connection aborted.` and terminates. | Terminate client if still running. | ✓ |
| **2003** | Client start with login but no server | Start the client with a loginID while no server is running. | Client prints: `ERROR - Can't setup connection! Terminating client.` and terminates. | Terminate client if needed. | ✓ |
| **2004** | Client connects to server | Start server (2001). Start client (2003). | Server logs client connection and `<loginID> has logged on.` Client displays `<loginID> has logged on.` | Terminate client and server unless continuing. | ✓ |
| **2005** | Client message echo | Start server and client (2004). Send a message from client. | Client displays: `<loginID> > message` and server logs: `Message received: message from <loginID>`. | Terminate server and client. | ✓ |
| **2006** | Multiple clients | Start server and multiple clients with different loginIDs. Exchange messages. | All client messages are echoed to all clients. Server messages appear as `SERVER MESSAGE> message`. | Terminate all clients and server. | ✓ |
| **2007** | Server quit command | Start the server. Type `#quit` in the server console. | Server terminates. | If still active, force terminate. | ✓ |
| **2008** | Server close behavior | Start server and client (2004). Type `#stop` then `#close` on server. | Server stops, disconnects client, and client prints `The server has shut down.` then terminates. | Terminate server if needed. | ✓ |
| **2009** | Server restart | Start server → `#close` → `#start`, then connect client. | Server restarts and listens normally. Client connects normally. | Terminate client and then `#quit` server. | ✓ |
| **2010** | Client quit command | Start server and client. Type `#quit` on client. | Client terminates. | Terminate if still active. | ✓ |
| **2011** | Client logoff command | Start server and connect one client. Type `#logoff` on client. | Client disconnects and prints `Connection closed.` | Type `#quit` to exit client. | ✓ |
| **2012** | Server on custom port | Start server with port `1234`. | Server prints: `Server listening for connections on port 1234.` | Use `#quit` to stop server. | ✓ |
| **2013** | Client to custom port | Start server on `1234`. Start client with `<loginID> <host> 1234`. | Client connects successfully. | Terminate server and client. | ✓ |
