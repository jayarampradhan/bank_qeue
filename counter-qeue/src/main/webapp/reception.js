function ApplicationModel(stompClient) {
  var self = this;

  self.username = ko.observable();
  self.counterQeues = ko.observable(new CounterQeuesModel());
  self.ticketQeues = ko.observable(new TicketQeuesModel());
  self.ticket = ko.observable(new TicketModel(stompClient));
  self.notifications = ko.observableArray();

  self.connect = function(){
	  stompClient.connect({}, function(frame) {

	      console.log('Connected ' + frame);
	      self.username(frame.headers['user-name']);
	      //Counters First Update
	      stompClient.subscribe("/app/counters", function(message) {
	    	  self.counterQeues().loadCounterDetails(JSON.parse(message.body));
	      });
	      //Ticket First Update
	      stompClient.subscribe("/app/tickets", function(message) {
	    	  self.ticketQeues().loadticketDetails(JSON.parse(message.body));
	      });
	      //Counters Update
	      stompClient.subscribe("/topic/bank.counter.*", function(message) {
	    	  self.counterQeues().refreshData(JSON.parse(message.body));
	      });
	      //Ticket Update
	      stompClient.subscribe("/topic/bank.tickets.*", function(message) {
	    	  self.ticketQeues().refreshData(JSON.parse(message.body));
	      });
	      stompClient.subscribe("/user/queue/counter/errors", function(message) {
	        self.pushNotification("Error " + message.body);
	      });
	      stompClient.subscribe("/user/queue/ticket/errors", function(message) {
		        self.pushNotification("Error " + message.body);
		 });
	    }, function(error) {
	      console.log("STOMP protocol error " + error);
	    });
  };

  self.pushNotification = function(text) {
    self.notifications.push({notification: text});
    if (self.notifications().length > 5) {
      self.notifications.shift();
    }
  };

}

//Counter feed model and its properties
function TicketQeuesModel() {
	  var self = this;

	  self.rows = ko.observableArray();

	  self.total = ko.computed(function() {
	    var result = 0;
	    for ( var i = 0; i < self.rows().length; i++) {
	      result ++;
	    }
	    return result;
	  });
	  var rowLookup = {};
	  
	  self.loadticketDetails = function(data){
		  for ( var i = 0; i < data.length; i++) {
			  var row = new TicketRow(data[i]);
			  self.rows.push(row);
			  rowLookup[row.id] = row;
		  }
	  };
	  
	 self.refreshData = function(data){
		 if (rowLookup[data.id]) {
		     rowLookup[data.id].refresh(data);
		 }else{
			 var row = new TicketRow(data);
			 self.rows.push(row);
			 rowLookup[row.id] = row;
		 }
	 };
};

//Counter feed model and its properties
function CounterQeuesModel() {
	  var self = this;

	  self.rows = ko.observableArray();

	  self.total = ko.computed(function() {
	    var result = 0;
	    for ( var i = 0; i < self.rows().length; i++) {
	      result ++;
	    }
	    return result;
	  });
	  var rowLookup = {};
	  
	  self.loadCounterDetails = function(data) {
		  for ( var i = 0; i < data.length; i++) {
			  var row = new CounterRow(data[i]);
			  self.rows.push(row);
			  rowLookup[row.counterId] = row;
		  }
	};
	
	self.refreshData = function(data){
		if (rowLookup[data.counterId]) {
		     rowLookup[data.counterId].refresh(data);
		 }else{
			 var row = new CounterRow(data);
			 self.rows.push(row);
			 rowLookup[row.counterId] = row;
		 }
	};
};

//Counter Row Details
function CounterRow(data) {
	  var self = this;

	  self.counterId = data.counterId;
	  self.counterType = data.counterType;
	  self.closed = data.closed;
	  self.onBreak = data.onBreak;
	  self.serving = data.serving;
	  self.counterStatus = ko.computed(function() {
		  	if(self.closed){
		  		return "Closed";
		  	}else if(self.onBreak){
		  		return "On Break";
		  	}else if(self.serving){
		  		return "Serving";
		  	}else{
		  		return "Free";
		  	}
		  });
	  self.counterOpenTime = data.counterOpenTime;
	  self.counterCloseTime = data.counterCloseTime;
	  self.ticket = new TicketRow(data.ticket || {});
	  self.refresh = function(newdata){
		  self.closed = newdata.closed;
		  self.onBreak = newdata.onBreak;
		  self.serving = newdata.serving;
		  self.counterStatus = ko.computed(function() {
			  	if(self.closed){
			  		return "Closed";
			  	}else if(self.onBreak){
			  		return "On Break";
			  	}else if(self.serving){
			  		return "Serving";
			  	}else{
			  		return "Free";
			  	}
			  });
		  self.counterOpenTime = newdata.counterOpenTime;
		  self.counterCloseTime = newdata.counterCloseTime;
		  self.ticket = new TicketRow(newdata.ticket || {});
	  };
	};
	
	//Counter Row Details
	function TicketRow(data) {
		  var self = this;

		  self.id = data.id;
		  self.details = data.details;
		  self.type = data.type;
		  self.createdOn = data.createdOn;
		  self.servedOn = data.servedOn;
		  self.closedOn = data.closedOn;
		  self.isClosed = data.isClosed;
		  self.isWaiting = data.isWaiting;
		  self.status = ko.computed(function() {
			  	if(self.servedOn == 0 || self.isWaiting){
			  		return "Waiting";
			  	}else if(self.isClosed){
			  		return "Closed";
			  	}else if(self.servedOn > 0){
			  		return "In Process";
			  	}else{
			  		return "Waiting";
			  	}
			  });
		  self.refresh = function(newdata){
			  self.createdOn = newdata.createdOn;
			  self.servedOn = newdata.servedOn;
			  self.closedOn = newdata.closedOn;
			  self.isClosed = newdata.isClosed;
			  self.isWaiting = newdata.isWaiting;
			  self.status = ko.computed(function() {
				  	if(self.servedOn == 0 || self.isWaiting){
				  		return "Waiting";
				  	}else if(self.isClosed){
				  		return "Closed";
				  	}else if(self.servedOn > 0){
				  		return "In Process";
				  	}else{
				  		return "Waiting";
				  	}
				  });
		  };
		};

//Ticket Modal and its properties
function TicketModel(stompClient) {
	  var self = this;
	  self.category = ko.observable(1);
	  self.description = ko.observable('');
	  self.error = ko.observable('');
	  
	  self.openCreateTicketDialog  = function() { self.showModal() }

	  self.showModal = function() {
		self.category = ko.observable(1);
		self.description = ko.observable('');
		self.error = ko.observable('');
	    $('#ticket-dialog').modal();
	  };

	  $('#ticket-dialog').on('shown', function () {
	    var input = $('#ticket-dialog input');
	    input.focus();
	    input.select();
	  });
	  
	  var validateTicket = function() {
	      if (isNaN(self.category()) || (self.category() < 1 && self.category() > 3)) {
	        self.error('Invalid Category');
	        return false;
	      }
	      return true;
	  };

	  self.create = function() {
	    if (!validateTicket()) {
	      return;
	    }
	    var ticket = {
	        "details" : self.description(),
	        "type" : self.category()
	      };
	    console.log(ticket);
	    stompClient.send("/app/createticket", {}, JSON.stringify(ticket));
	    $('#ticket-dialog').modal('hide');
	  };
	  
	}