module.exports = function(app, User)
{
	//register
	
	app.post('/api/register', function(req, res){
		User.findOne({id: req.body.id}, function(err, user){
			if(err)
				return res.json({result: 0});
			if(user) 
				return res.json({result: 1});
			var user = new User();
			user.id = req.body.id;
			user.password = req.body.password;
			user.userName = req.body.userName;
			user.contact = [];
			user.photo = [];

			user.save(function(err){
				if(err){
					console.error(err);
					res.json({result: 0});
					return;
				}

				res.json({result : 2});
			});
		});
	});

	//login
	
	app.get('/api/login/:id/:password', function(req, res){
		User.findOne({id: req.params.id}, function(err, user){
			if(err)
				return res.json({result: 0});
			if(!user)
				return res.json({result: 1});
			if(user.password === req.params.password)
				return res.json({result: 2});

			return res.json({result: 3});
		});
	});

	app.get('/api/contact/:id', function(req, res){
		User.findOne({id: req.params.id}, function(err, user){
			console.log(user);
			console.log(user.contact);
			if(!user.contact)
				return res.json([]);
			res.json(user.contact);
			//console.log(user.contact);
		});
	});

	app.get('/api/photo/:id', function(req, res){
		User.findOne({id: req.params.id}, function(err, user){
			console.log(user);
			console.log(user.photo);

			res.json(user.photo);
			console.log(user.contact);
		});
	});

	app.post('/api/photo/add', function(req, res){
		console.log("adsf");
		var id = req.body.id;
		var fileString = req.body.fileString;
		var option = {upsert : true, new : true, useFindAndModify : false}

		newData = {fileString : fileString};
		console.log(newData);
		User.findOneAndUpdate({id: req.body.id}, {$push : {photo : newData}}, option, function(error, change){
			if(error){
				console.log(error);
				res.json({result: 0});
			}
			else{
				console.log("success");
				res.json({result: 1});
			}
		});
	});

	app.post('/api/contact/add', function(req, res){
		console.log("adsfasdf");
		var id = req.body.id;
		var name = req.body.name;
		var number = req.body.number;
		var option = {upsert : true, new : true, useFindAndModify : false}

		newData = {name : name, number : number};
		console.log(newData);
		User.findOneAndUpdate({id: req.body.id}, {$push : {contact : newData}}, option, function(error, change){
			if(error){
				console.log(error);
				res.json({result: 0});
			}
			else{
				console.log("success");
				res.json({result: 1});
			}
		});
	});

}