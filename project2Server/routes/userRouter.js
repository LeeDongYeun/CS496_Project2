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
			//res.json(user.contact);

			var data = {name : "a", number : "01000000000"};
			var data2 = {name : "b", number : "01011111111"};
			user.contact.push(data);
			user.contact.push(data2);
			res.json(user.contact);
			console.log(user.contact);
		});
	});
	
}